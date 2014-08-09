(ns obr-clj.core-test
  (:require [clojure.test :refer :all]
            [obr-clj.core :refer :all]
            [clojure.java.io :as io]))

(deftest repository-test
  (testing "Testing create-repo from local file."
    (let [repo-file "test/resources/index.xml"
          in (io/input-stream repo-file)
          repo (create-repo in)]
      (is (= (.getName repo)
             "Untitled"))
      (is (nil? (.getURI repo)))))
  (testing "Testing create-repo from remote file."
    (let [repo-file "http://ec2-54-191-72-254.us-west-2.compute.amazonaws.com:8080/test-index.xml"
          repo (create-repo repo-file)]
      (is (= (.getName repo)
             "Untitled"))
      (is (= (.getURI repo)
             repo-file)))))
