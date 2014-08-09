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
      (is (nil? (.getURI repo))))))
