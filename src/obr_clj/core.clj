(ns obr-clj.core
  (:import [java.net URL]
           [org.apache.felix.bundlerepository.impl DataModelHelperImpl]))

(defmulti create-repo
  "Returns a Felix bundle-repository repository."
  class)

(defmethod create-repo java.io.InputStream
  [is]
  (.repository (DataModelHelperImpl.) is))

(defmethod create-repo java.net.URL
  [url]
  (.repository (DataModelHelperImpl.) url))

(defmethod create-repo :default
  [uri]
  (.repository (DataModelHelperImpl.) (URL. uri)))
