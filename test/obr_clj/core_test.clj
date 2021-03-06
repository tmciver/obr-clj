(ns obr-clj.core-test
  (:require [clojure.test :refer :all]
            [obr-clj.core :refer :all]
            [clojure.java.io :as io]))

(deftest create-repo-test
  (testing "Testing create-repo from local file."
    (let [repo-file "test/resources/index.xml"
          in (io/input-stream repo-file)
          repo (create-repo in)]
      (is (instance? org.apache.felix.bundlerepository.Repository repo))
      (is (= (.getName repo)
             "Untitled"))
      (is (nil? (.getURI repo)))))
  (testing "Testing create-repo from URL."
    (let [repo-url (io/resource "resources/index.xml")
          repo (create-repo repo-url)]
      (is (instance? org.apache.felix.bundlerepository.Repository repo))
      (is (= (.getName repo)
             "Untitled"))
      (is (= (.getURI repo)
             (str repo-url)))
      (is (= (count (.getResources repo))
             5)))))

(deftest resource-test
  (testing "Testing create-resource."
    (let [bundle-url (io/resource "resources/test-bundle.jar")
          bundle-file-size (-> bundle-url .toURI java.io.File. .length)
          res (create-resource bundle-url)]
      (is (instance? org.apache.felix.bundlerepository.Resource res))
      (is (= (.getURI res)
          (str (.toURI bundle-url))))
      (is (= (.getSize res)
          bundle-file-size))
      (is (.getSymbolicName res)
          "org.foo.shape.triangle")
      (is (str (.getVersion res))
          "4.0.0")
      ;; test setting of URI
      (let [uri "http://example.com/path/to/my-bundle.jar"
            res (set-resource-uri res uri)]
        (is (= (.getURI res) uri))))))

(deftest add-resource-test
  (testing "Test add resource."
    (let [repo-url (io/resource "resources/index.xml")
          repo (create-repo repo-url)
          bundle-url (io/resource "resources/test-bundle.jar")
          res (create-resource bundle-url)
          res-sym-name (.getSymbolicName res)]
      (add-resource repo res)
      (is (= (count (.getResources repo))
             6))
      (is (= (count (filter #(= (.getSymbolicName %) res-sym-name)
                            (.getResources repo)))
             1)))))

(deftest write-repo-test
  (testing "Write repository to file."
    ;; currently this test only reads and then writes an empty repository file
    (let [repo-url (io/resource "resources/empty-index.xml")
          repo (create-repo repo-url)
          tmp-file (java.io.File/createTempFile "repo-" ".xml")]
      (add-resource repo (create-resource (io/resource "resources/test-bundle.jar")))
      (with-open [wrtr (io/writer tmp-file)]
        (write-repo repo wrtr)))))
