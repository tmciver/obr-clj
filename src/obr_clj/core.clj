(ns obr-clj.core
  (:import [org.apache.felix.bundlerepository.impl DataModelHelperImpl]))

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
  [url]
  (.repository (DataModelHelperImpl.) (java.net.URL. url)))
