(ns isolkkariopen.settings)

(defn- megabytes [^long n] (* n 1024 1024))

(def settings {
  :cam-url             "http://www.athene.fi/olocam/latest.jpg"
  :history-size        (-> 400 megabytes)
  
  :buzz-multiplier     2
  :noise-tolerance     5
  :max-history-results 100
  :olkkari-open-x      600
  :olkkari-open-y      400
  :update-interval-ms  10000
})