(ns isolkkariopen.web
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.core.async :refer [go timeout]]
            [clojure.data.json :as json]
            [monger.core :as mg]
            [ring.adapter.jetty :refer [run-jetty]]
            [environ.core :refer [env]]
            [isolkkariopen.history :as history]
            [isolkkariopen.olocam :as olocam]
            [isolkkariopen.template :as template]
            [isolkkariopen.settings :refer [settings]]))

(defn connect-db! [environment]
  (if (= environment "production")
    (mg/connect-via-uri! (env :mongo-uri))))
    (mg/connect! { :host "localhost" :port 27017 })
  (mg/set-db! (mg/get-db "isolkkariopen"))

(defn periodically [f periodMillis]  
  (go
    (loop []
      (<! (timeout periodMillis))
      (f)
      (recur))))

(def jsonheader { :headers {"Content-Type" "application/json"} })

(defroutes app
  (GET "/api/history" []
    (merge jsonheader
      {:status 200
       :body (json/write-str (history/entries))}))

  (GET "/api/now" []
    (merge jsonheader
      {:status 200
       :body (json/write-str (history/now))}))

  (GET "/" []
    {:headers {"Content-Type" "text/html"}
     :status 200
     :body (template/index (history/now))}))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
      (catch Exception e
        (merge jsonheader
          {:status 500
           :body (json/write-str {:error (. e getMessage)})})))))

(defn -main [& args]
  (connect-db! (env :environment))
  (let [port (Integer. (or (env :port) 5000))]
    (periodically history/update! (:update-interval-ms settings))
    (run-jetty
      (-> #'app
        wrap-error-page
        (site))
      {:port port :join? false})))
