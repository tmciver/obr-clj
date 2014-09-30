(ns obr-clj.core
  (:require [clojure.java.io :as io])
  (:import [org.apache.felix.bundlerepository Resource]
           [org.apache.felix.bundlerepository.impl DataModelHelperImpl
            ResourceImpl RepositoryImpl]))

(defmulti create-repo
  "Returns a Felix bundle-repository repository."
  class)

(defmethod create-repo java.io.InputStream
  [is]
  (.repository (DataModelHelperImpl.) is))

(defmethod create-repo java.net.URL
  [url]
  (.repository (DataModelHelperImpl.) url))

(defmethod create-repo String
  [url]
  (.repository (DataModelHelperImpl.) (java.net.URL. url)))

(defmulti create-resource
  "Creates a Resource from the given input, either a java.net.URL or a URL as
string."
  class)

(defmethod create-resource java.net.URL
  [url]
  (let [res (.createResource (DataModelHelperImpl.) url)
        res (cast ResourceImpl res)
        res-file (java.io.File. (.toURI url))]
    (doto res
      (.put Resource/SIZE (str (.length res-file)))
      (.put Resource/URI (.toString url)))))

(defmethod create-resource String
  [url]
  (create-resource (java.net.URL. url))
  ;(.createResource (DataModelHelperImpl.) (java.net.URL. url))
  )

(defn add-resource
  "Update the given Repository with the given Resource."
  [repo res]
  (doto repo
      (.addResource res)
      (.setLastModified (System/currentTimeMillis))))

(defn write-repo
  ([repo]
   (.writeRepository (DataModelHelperImpl.) repo))
  ([repo wrtr]
   (.writeRepository (DataModelHelperImpl.) repo wrtr)))

(defn index
  "Creates an OBR index named \"repository.xml\" in the current directory from
  the bundles in the given directory."
  [dir]
  (let [jar-urls (->> (file-seq (io/file dir))
                      (filter #(.endsWith (str %) ".jar"))
                      (map #(.toURL (.toURI %))))
        resources (map create-resource jar-urls)
        repo (reduce #(add-resource %1 %2) (RepositoryImpl.) resources)]
    (with-open [wrtr (io/writer "repository.xml")]
      (write-repo repo wrtr))))
