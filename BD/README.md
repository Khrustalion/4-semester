# M3201_KhrustalevND

## Функциональные требования
### Cайт для заказа вещей с poizon

### Товары

- Товары должны иметь возможность быть отфильтрованы по бренду, категории, подкатегории, цвету, размеру, основному материалу, стране производителя

- Товары должны иметь возможность быть отсортированными по цене, популярности

- У пользователей должна быть возможность просматривать историю цен на каждый товар

### Покупатели

- Пользователи должны иметь возможность зарегистрироваться, введя логин и пароль

- Пользовтель должен иметь возможность войти в систему, введя верный логин и пароль

- Зарегестрированные пользователи обязаны внести основную информацию о себе

- Покупатель должен иметь возможность добавлять товары в понравившиеся товары и в корзину

- Покупатель должке иметь возможность оформить заказ товаров, который в текущий момент лежат в корзине.

- у покупателя, при оплате онлайн, должны сохранятся данные карты для следующих покупок

- Если пользователь купил товар, то он может оставить отзыв на этот товар


### Заказ

- Заказы должны иметь возможность фильтроваться по дате заказа

- Покупатель должен иметь возможность просматривать историю своих заказов

- Покупатель должен иметь возможность списать накопленные балла лояльности

- Покупатель должен иметь возможность применять промокоды, которые присутсвуют на его аккаунте (При использовании промокод сгорает)

- Покупатель должен иметь возможность отсматривать текущий стаус заказа

Статусы

1) Обрабатывается
2) В пути по Китаю
3) Поступил на склад в Китае
4) В пути через границу
5) Поступил на склад в России
6) В пути по Росии

### Программа лояльности

- Пользователь за каждый заказ должен получать баллы, которыми он в будущем может оплатить часть заказа

- Покупатели должны быть ранжироваться по общей сумме заказов и, в соответсвии с ней, получать свой статус

- Баллы должны иметь определенный срок дейтсвия с заказа - 1 год, после чего должны сгорать

В зависимости от статуса пользователь получает различное количество бонусов за заказ

Статусы пользователей, процент от суммы заказа, который возвращается в виде баллов и необходимая общая сумма заказов

- базовый - 0.5% (0 - 50 000 руб.)
- уверенный - 1% (50 000 - 100 000 руб.)
- продвинутый - 3%(100 000 - 300 000 руб.)
- легендарный - 5% (300 000 руб. и больше)

### Продавцы

- Продавцы должны уметь продавать товары по разным ценам
- Пользователи могут оставлять оценки и комментарии продавцам
- Продавцы должны иметь возможность учавствовать в акциях

### Акции

- Акции распространяются на определенную категорию товаров с фиксированной скидкой

### Прочие скидки

- Некоторые пользователи должны иметь персональные промокоды, которые могут применять к своему заказу

```plantuml
@startuml


entity users {
    +user_id : int <PK>
--
    login : text
    password : text
    name : text
    email : text // nullable
    phone : text // nullable
    status : text
}

entity addresses {
    +address_id : int <PK>
--
    user_id : int <FK>
    country : text
    region : text
    city : text
    house_number : text
    apartment_number : int
}

entity products {
    +product_id : int <PK>
--
    product_subcategory_id : int <FK>
    name : text
    product_number : int
    brand : text
    description : text
    color : text
    material : text
    country_origin : text
}

entity product_details {
    +product_details_id : int <PK>
--
    product_id : int <FK>
    seller_id : int <FK>
    size_eu : int
    price : decimal
}

entity product_subcategories {
    +product_subcategory_id : int <PK>
--
    product_category_id : int <FK>
    name : text
}

entity product_categories {
    +product_category_id : int <PK>
--
    name : text
}

entity product_price_histories {
    +product_price_history_id : int <PK>
--
    product_id : int <FK>
    price : decimal
    change_date : datetime
}

entity favorites {
    +favorite_id : int <PK>
--
    user_id : int <FK>
    product_id : int <FK>
}

entity cart_items {
    +cart_item_id : int <PK>
--
    user_id : int <FK>
    product_id : int <FK>
    quantity : int
}

entity payment_cards {
    +payment_card_id : int <PK>
--
    user_id : int <FK>
    card_number : text
    expiration_date : text
    save_date : datetime
}

entity review_products {
    +review_product_id : int <PK>
--
    user_id : int <FK>
    order_header_id : int <FK>
    product_id : int <FK>
    matching_size : int
    quality : int
    convenience : int
    comment : text
    rating : int
    create_date : datetime
}

entity order_headers {
    +order_header_id : int <PK>
--
    user_id : int <FK>
    address_id : int <FK>
    applied_promo_id : int <FK>
    payment_card_id : int <FK>
    order_date : datetime
    total_amount : decimal
    used_loyalty_points : int
}

entity order_details {
    +order_details_id : int <PK>
--
    order_header_id : int <FK>
    product_details_id : int <FK>
    quantity : int
}

entity tracking_order_headers {
    +tracking_order_header_id : int <PK>
--
    order_header_id : int <FK>
    status : text
    start_date : datetime
    end_date : datetime
}

entity promo_codes {
    +promo_code_id : int <PK>
--
    user_id : int <FK>
    code : text
    discount_percent : int
    is_personal : bool
    expires_date : datetime
    used : bool
}

entity loyalty_point_histories {
    +loyalty_point_history_id : int <PK>
--
    user_id : int <FK>
    order_header_id : int <FK>
    points : int
    earned_date : datetime
    expires_date : datetime
    spent : bool
}

entity sellers {
    +seller_id : int <PK>
--
    user_id : int <FK>
}

entity review_sellers {
    +review_seller_id : int <PK>
--
    user_id : int <FK>
    seller_id : int <FK>
    rating : int
    comment : text
    create_date : datetime
}

entity promotion_headers {
    +promotion_header_id : int <PK>
--
    product_category_id : int <FK>
    percentage_discount : int
}

entity promotion_details {
    +promotion_details_id : int <PK>
--
    promotion_header_id : int <FK>
    seller_id : int <FK>
}

addresses }o--|| users

products }o--|| product_subcategories

product_subcategories }o--|| product_categories

product_details }o--|| products
product_details }o--|| sellers

product_price_histories }o--|| products

favorites }o--|| users
favorites }o--|| products

cart_items }o--|| users
cart_items }o--|| products

payment_cards }o--|| users

review_products }o--|| users
review_products }o--|| products
review_products }o--|| order_headers

order_headers }o--|| users
order_headers }o--|| addresses
order_headers }o--|| promo_codes
order_headers }o--|| payment_cards

order_details }o--|| order_headers
order_details }o--|| product_details

tracking_order_headers }o--|| order_headers

promo_codes }o--|| users

loyalty_point_histories }o--|| users
loyalty_point_histories }o--|| order_headers

sellers }o--|| users

review_sellers }o--|| users
review_sellers }o--|| sellers

promotion_headers }o--|| product_categories

promotion_details }o--|| promotion_headers
promotion_details }o--|| sellers

@enduml
'''
