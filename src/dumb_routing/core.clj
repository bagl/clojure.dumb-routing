(ns dumb-routing.core
  (:require [dumb-routing.util :refer [find-route
                                       build-handler]]))

(defn basic-handler [routes & [custom-not-found]]
  (fn [request]
    (let [uri (:uri request)
          not-found (or custom-not-found
                        (fn [_] {:status 404 :headers {} :body "not found"}))
          route (find-route routes uri)
          handler (if route (build-handler route uri) not-found)]
        (handler request))))
