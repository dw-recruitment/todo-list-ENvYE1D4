(ns todo.todos.db
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [datomic.api :as d]))

(defonce uri (str "datomic:mem://" (gensym "todos")))
(d/create-database uri)
(def conn (d/connect uri))

(def schema-tx (->> "schema.edn"
                    io/resource
                    slurp
                    (edn/read-string {:readers *data-readers*})))

@(d/transact conn schema-tx)

(defn create-todo [description]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :todo/description description
                      :todo/completed? false}]))

(defn find-all-todos [db]
  (->> (d/q '[:find ?id
              :where [?id :todo/description]]
            db)
       (map first)
       (map #(d/entity db %))))
