(ns isolkkariopen.olocam-test
  (:require [clojure.test :refer :all]
            [isolkkariopen.olocam :as olocam]))

(deftest latest-pic-dimensions
  (is
    (= 
      [640 480]
      [(. (olocam/latest-pic) (getWidth)) (. (olocam/latest-pic) (getHeight))])
    "Can fetch a 640x480 picture from Olocam"))