(ns obr-clj.core
  (:require [clojure.reflect :as r])
  (:import [org.apache.felix.bundlerepository.impl RepositoryImpl]))

(defn create-repo
  "Returns a Felix bundle-repository repository with the given URI."
  [uri]
  (let [repo (RepositoryImpl.)
        methods (-> repo
                    .getClass
                    .getDeclaredMethods)
        setURI (->> methods
                    (filter #(= "setURI" (.getName %)))
                    first)
        setURI (doto setURI (.setAccessible true))
        args (into-array [uri])]
    (.invoke setURI repo args)
    repo))
