(ns isolkkariopen.web
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [ring.adapter.jetty :refer [run-jetty]]
            [environ.core :refer [env]]
            [isolkkariopen.olocam :refer [olkkari-open?]]
            [isolkkariopen.template :as template]))

(def jsonheader
  { :headers {"Content-Type" "application/json"} })

(defroutes app
  (GET "/api/open" []
    (merge jsonheader
      {:status 200
       :body (json/write-str {:open (olkkari-open?)})}))

  (GET "/" []
    {:headers {"Content-Type" "text/html"}
     :status 200
     :body (template/index (olkkari-open?))}))

(defn wrap-error-page [handler]
  (fn [req]
    (try (handler req)
      (catch Exception e
        (merge jsonheader
          {:status 500
           :body (json/write-str {:error (. e getMessage)})})))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (run-jetty
      (-> #'app
        wrap-error-page
        (site))
      {:port port :join? false})))
