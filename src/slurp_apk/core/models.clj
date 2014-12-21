(ns slurp-apk.core.models
  (:require [clojure.string :as str])
  (:use korma.config
        korma.core
        korma.db
        slurp-apk.core.utils
        ))

(defdb db-spec (sqlite3 {:db db-path}))

(defentity apks)

(defn find-apk-by-url [apk_url]
  (first (select apks
                 (where {:apk_url apk_url})
                 (limit 1))))

(defn create-apk [apk_url file_path file_name package_name digest]
  (insert apks
          (values {
                   :apk_url      apk_url
                   :file_path    file_path
                   :file_name    file_name
                   :package_name package_name
                   :digest       digest
                   })))