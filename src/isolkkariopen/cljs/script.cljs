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
  (def historyJs (clj->js (take 15 (reverse history))))
  
  (def buzzArr (map #(.-buzz %) historyJs))

  (def timeArr
    (map #(.format % "HH:mm:ss")
      (map js/moment (map #(.-time %) historyJs))))
  
  (def options
    (clj->js {
      :pointDotRadius 1
      :scaleOverride true
      :scaleSteps 5
      :scaleStepWidth 20
      :scaleStartValue 0
      :scaleLabel "<%=value%> %"
    }))

  (def data
    (clj->js {
      :datasets [{
          :fillColor   "rgba(100,255,100,0.8)"
          :strokeColor "rgba(220,220,220,1)"
          :pointColor  "rgba(220,220,220,1)"
          :pointStrokeColor "#fff"
          :data buzzArr }]
      :labels timeArr }))

  ; Draw history line chart
  (.Line
    (new js/Chart (.getContext (by-id "history") "2d"))
    data options))

(get-history! visualize-history log-error)