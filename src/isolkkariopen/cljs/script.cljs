(ns isolkkariopen.script
  (:require [ajax.core :refer [GET]]))

(defn by-id [id]
  (.getElementById js/document id))

(defn log-error [{:keys [status status-text response]}]
  (.log js/console (str "Got " status " " status-text ", response: " response)))

(defn get-history! [handler error-handler]
  (GET "api/history" {
    :handler handler
    :error-handler error-handler }))

(defn visualize-history [history]
  (def buzzSeq (map #(.-buzz %) (clj->js history)))
  (def timeSeq (map #(.-time %) (clj->js history)))
  
  (def options (clj->js {:pointDotRadius 1}))

  (def data
    (clj->js {
      :datasets [{
          :fillColor   "rgba(100,255,100,0.8)"
          :strokeColor "rgba(220,220,220,1)"
          :pointColor  "rgba(220,220,220,1)"
          :pointStrokeColor "#fff"
          :data buzzSeq }]
      :labels timeSeq }))

  ; Draw history line chart
  (.Line
    (new js/Chart (.getContext (by-id "history") "2d"))
    data))

(get-history! visualize-history log-error)