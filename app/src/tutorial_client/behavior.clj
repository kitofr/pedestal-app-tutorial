(ns ^:shared tutorial-client.behavior
    (:require [clojure.string :as string]
              [io.pedestal.app :as app]
              [io.pedestal.app.messages :as msg]))

(defn set-value-transform [old-value message]
  (:value message))

(defn inc-transform [old-value _]
  ((fnil inc 0) old-value))

(defn init-main [_]
  [[:transform-enable [:main :my-counter] :inc [{msg/topic [:my-counter]}]]])

(defn swap-transform  [_ message]
  (:value message))

(defn publish-counter [count]
  [{msg/type :swap msg/topic [:other-counters] :value count}])

(def example-app
  {:version 2
   :transform [[:inc [:my-counter] inc-transform]
               [:swap [:**]        swap-transform]]
   :effect #{[#{[:my-counter]} publish-counter :single-val]}
   :emit [{:init init-main}
          [#{[:my-counter] [:other-counters :*]} (app/default-emitter [:main])]]})


