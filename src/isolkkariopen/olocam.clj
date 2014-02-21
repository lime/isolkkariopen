(ns isolkkariopen.olocam
  (import [javax.imageio ImageIO]
          [java.awt Color]
          [java.net URL]))

(def url (new URL "http://www.athene.fi/olocam/latest.jpg"))

(defn- fetch-pic! [] (ImageIO/read url))

(defn pixel [img x y]
  (new Color (. img (getRGB x y)) true))

(defn databuffer [bufferedImg]
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

(defn buzz [pic1 pic2]
  (float
    (normed-inequality (databuffer pic1) (databuffer pic2))))

(defn olkkari-open? [pic]
  (not= Color/black (pixel pic 500 100)))



(def history-size 50)

(def prev-pic (atom (fetch-pic!)))
(def activity (atom (list {:open false :buzz 0.0})))

(defn current-activity [] (first @activity))

(defn create-activity-entry [currPic prevPic]
  {:open (olkkari-open? currPic)
   :buzz (buzz currPic prevPic)})

(defn fetch-as-entry! []
  (let [currPic (fetch-pic!) prevPic @prev-pic]
    (reset! prev-pic currPic)
    (create-activity-entry currPic prevPic)))

(defn update-activity! []
  (reset! activity
    (take history-size (cons (fetch-as-entry!) @activity))))