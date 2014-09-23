(ns obr-clj.core
  (:import [org.apache.felix.bundlerepository Resource]
           [org.apache.felix.bundlerepository.impl DataModelHelperImpl
            ResourceImpl]))

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
