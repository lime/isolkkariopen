(ns isolkkariopen.olocam-test
  (:require [clojure.test :refer :all]
            [isolkkariopen.olocam :as olocam])
  (import [java.awt.image DataBufferInt]))

;(deftest latest-pic-dimensions
;  (is (= 
;    [640 480]
;    [(. (olocam/fetch-pic!) (getWidth)) (. (olocam/fetch-pic!) (getHeight))])
;  "Can fetch a 640x480 picture from Olocam"))

(def emptyThree   (new DataBufferInt 3))
(def threeNines   (new DataBufferInt (int-array [9, 9, 9]) 3))
(def threeFours   (new DataBufferInt (int-array [4, 4, 4]) 3))
(def fourNineFour (new DataBufferInt (int-array [4, 9, 4]) 3))

(deftest equality-buffer
  (is (=
    [true true true]
    (olocam/equality-buffer emptyThree emptyThree)))
  (is (=
    [false false false]
    (olocam/equality-buffer threeNines threeFours)))
  (is (=
    [false true false]
    (olocam/equality-buffer threeNines fourNineFour)))
  (is (=
    [true false true]
    (olocam/equality-buffer threeFours fourNineFour))))

(deftest normed-inequality
  (is (= 0   (olocam/normed-inequality emptyThree emptyThree)))
  (is (= 1   (olocam/normed-inequality threeNines threeFours)))
  (is (= 2/3 (olocam/normed-inequality threeNines fourNineFour)))
  (is (= 1/3 (olocam/normed-inequality threeFours fourNineFour))))

