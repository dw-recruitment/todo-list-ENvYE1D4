(ns todo.core
  (:require [clojure.java.io :as io]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route :refer [router]]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ns-tracker.core :refer [ns-tracker]]
            [ring.util.response :as ring-resp]))

(defn set-content-type
  [response content-type]
  (assoc-in response [:headers "Content-Type"] content-type))

(defn home-page
  ([req]
   (let [gif (io/file (io/resource "public/under-construction.gif"))]
     (set-content-type (ring-resp/response gif) "image/gif"))))

(defn about-page
  ([req]
   (let [pdf (io/file (io/resource "public/dev-candidate-exercise.pdf"))]
     (set-content-type (ring-resp/response pdf) "application/pdf"))))

(defroutes routes
  [[["/"
     {:get home-page}
     ["/about"
      {:get about-page}]]]])

(def track-namespaces
  (ns-tracker ["src"]))

(def service {::http/interceptors [http/log-request
                                   http/not-found
                                   (router (fn []
                                             (doseq [ns-sym (track-namespaces)]
                                               (require ns-sym :reload))
                                             routes))]
              ::http/port 8080})

(defn -main
  [& args]
  (-> service
      http/create-server
      http/start))
