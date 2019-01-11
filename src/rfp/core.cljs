(ns rfp.core
     (:require [reagent.core :as reagent]
               [re-frame.core :as rf]))

;; --- D2 - Event Handlers ---

(rf/reg-event-db :init
                 (fn [_ _]
                   {:username "username"
                    :name "Test User"}))

(rf/reg-event-db :change-username
                 (fn [db event]
                   (assoc db :username (second event))))

(rf/reg-event-db :change-name
                 (fn [db event]
                   (assoc db :name (second event))))

(defn do-save [{:keys [db]} _]
  {:db (assoc db :saved (conj (:saved db) {:username @(rf/subscribe [:username])
                                           :name     @(rf/subscribe [:name])
                                           :id        (random-uuid)}))})

(defn do-delete [{:keys [db]} [_ val]]
  {:db (assoc db :saved (remove #(= val %) (:saved db)))})

(rf/reg-event-fx :save
                  do-save)

(rf/reg-event-fx :delete
                 do-delete)

;; --- D4 - Query ---

(rf/reg-sub :username
            (fn [db _]
              (:username db)))

(rf/reg-sub :name
            (fn [db _]
              (:name db)))

(rf/reg-sub :saved
            (fn [db _]
              (:saved db)))

;; --- D5 - View ---

(defn delete-user [i]
   [:button {:on-click #(rf/dispatch [:delete i])} "Remove"])

(defn ui
      []
      [:div
       [:h1 "some testing"]
       [:div
        [:span "Username: "]
        [:input {:type "text"
                 :value @(rf/subscribe [:username])
                 :on-change #(rf/dispatch [:change-username (-> % .-target .-value)])}]
        [:label "   ::   " @(rf/subscribe [:username])]]
       [:br]
       [:div
        [:span "Name: "]
        [:input {:type "text"
                 :value @(rf/subscribe [:name])
                 :on-change #(rf/dispatch [:change-name (-> % .-target .-value)])}]
        [:label "   ::   " @(rf/subscribe [:name])]]
       [:div
        [:button {:on-click #(rf/dispatch [:save])}
         "Save"]]
       [:br]
       [:div
        (let [saved @(rf/subscribe [:saved])]
          [:ul
           (map (fn [val] ^{:key (:id val)} [:li (:name val) " " (:username val) " " (delete-user val)]) saved)])]])

(defn ^:export run
      []
  (rf/dispatch-sync [:init])
  (reagent/render [ui] (js/document.getElementById "app")))