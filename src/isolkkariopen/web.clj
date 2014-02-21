(ns isolkkariopen.web
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.core.async :refer [go timeout]]
            [clojure.data.json :as json]
            [ring.adapter.jetty :refer [run-jetty]]
            [environ.core :refer [env]]
            [isolkkariopen.olocam :as olocam]
            [isolkkariopen.template :as template]))

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
       :body (json/write-str @olocam/activity)}))

  (GET "/api/now" []
    (merge jsonheader
      {:status 200
       :body (json/write-str (olocam/current-activity))}))

  (GET "/" []
    {:headers {"Content-Type" "text/html"}
     :status 200
     :body (template/index (olocam/current-activity))}))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
      (catch Exception e
        (merge jsonheader
          {:status 500
           :body (json/write-str {:error (. e getMessage)})})))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (periodically olocam/update-activity! 10000)
    (run-jetty
      (-> #'app
        wrap-error-page
        (site))
      {:port port :join? false})))
