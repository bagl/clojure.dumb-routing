(ns dumb-routing.core
  (:require [dumb-routing.util :as util]))

(defn dumb-handler [routes & [custom-not-found]]
  (fn [request]
    (let [uri (:uri request)
          not-found (or custom-not-found
                        (fn [_] {:status 404 :headers {} :body "not found"}))
          route (util/find-route routes uri)
          handler (if route (util/build-handler route uri) not-found)]
        (handler request))))
