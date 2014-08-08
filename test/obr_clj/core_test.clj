(ns obr-clj.core-test
  (:require [clojure.test :refer :all]
            [obr-clj.core :refer :all]))

(deftest repository-test
  (testing "Testing create-repository"
    (let [uri "http://example.com/repsository.xml"]
      (is (= (.getURI (create-repo uri))
             uri)))))
