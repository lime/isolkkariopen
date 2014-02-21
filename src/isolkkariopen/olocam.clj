(ns isolkkariopen.olocam
  (import [javax.imageio ImageIO]
          [java.awt Color]
          [java.net URL]))

(def url (new URL "http://www.athene.fi/olocam/latest.jpg"))

(defn- fetch-pic! [] (ImageIO/read url))

(def history (atom [(fetch-pic!) (fetch-pic!)]))

(defn curr-pic [] (get @history 0))
(defn prev-pic [] (get @history 1))

(defn update! []
  (let [newHistory [(fetch-pic!) (curr-pic)]]
    (reset! history newHistory)))

(defn pixel [img x y]
  (new Color (. img (getRGB x y)) true))

(defn dataBuffer [bufferedImg]
  (. (. bufferedImg (getRaster)) getDataBuffer))

(defn equality-buffer [buf1 buf2]
  "Boolean buffer through element equality comparison"
  (map
    #(= (. buf1 (getElem %)) (. buf2 (getElem %)))
    (range 0 (. buf1 (getSize)))))  

(defn normed-inequality [buf1 buf2]
  "Fractional amount of inequal elements between 0 and 1"

  (def eqbf (equality-buffer buf1 buf2))
  
  (if (zero? (count eqbf)) 0
    (/
      (count (filter false? eqbf))
      (count eqbf))))

(defn buzz []
  (float
    (normed-inequality (dataBuffer (curr-pic)) (dataBuffer (prev-pic)))))

(defn olkkari-open? []
  (not= Color/black (pixel (curr-pic) 500 100)))