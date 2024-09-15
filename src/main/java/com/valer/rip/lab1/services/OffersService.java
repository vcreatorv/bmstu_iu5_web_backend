package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class OffersService {

        private List<Map<String, ? extends Object>> offers;

        @PostConstruct
        public void init() {
                offers = List.of(
                                Map.of(
                                        "id", "1",
                                        "title", "Облачное видеонаблюдение",
                                        "imageURL", "http://127.0.0.1:9000/lab1/video-monitoring.webp",
                                        "price", 1129,
                                        "monthlyPayment", true,
                                        "unit", "шт",
                                        "description",
                                        "Система видеонаблюдения помогает предотвращать преступления, контролировать сотрудников и отслеживать поток посетителей.<br><br>- Хранение записи в облаке до 14 дней <br>- Уведомления о движениях и звуках"),

                                Map.of(
                                        "id", "2",
                                        "title", "Виртуальная АТС",
                                        "imageURL", "http://127.0.0.1:9000/lab1/virtual-ats.webp",
                                        "price", 350,
                                        "monthlyPayment", true,
                                        "unit", "шт",
                                        "description",
                                        "Виртуальная АТС даёт возможность обрабатывать до 100 входящих вызовов одновременно, настроить голосовое приветствие и умное распределение вызовов между отделами, сотрудниками или регионами.<br><br>- До 20 входящих звонков одновременно<br>- Статистика по принятым и пропущенным звонкам"),

                                Map.of(
                                        "id", "3",
                                        "title", "Прокладка интернет-кабеля",
                                        "imageURL", "http://127.0.0.1:9000/lab1/beeline-cable.jpg",
                                        "price", 500,
                                        "monthlyPayment", false,
                                        "unit", "метр",
                                        "description",
                                        "Наши специалисты проведут кабель от точки входа в дом до вашего устройства и настроят интернет-соединение, чтобы вы могли пользоваться всеми преимуществами скоростного интернета.<br><br>- Тип подлключения FTTB<br>- Оптоволоконный материал"),

                                Map.of(
                                        "id", "4",
                                        "title", "Подключение статического IP-адреса",
                                        "imageURL", "http://127.0.0.1:9000/lab1/static-ip.webp",
                                        "price", 150,
                                        "monthlyPayment", true,
                                        "unit", "шт",
                                        "description",
                                        "Постоянный IP-адрес с уникальным идентификатором, который определяется глобально во всей сети.<br><br>- Интернет-протокол ipv4"),

                                Map.of(
                                        "id", "5",
                                        "title", "Установка и подключение PLC-адаптера",
                                        "imageURL", "http://127.0.0.1:9000/lab1/ip-tv.webp",
                                        "price", 2899,
                                        "monthlyPayment", false,
                                        "unit", "шт",
                                        "description",
                                        "PLC-адаптеры – оптимальное решение, которое позволит подключить Домашнее телевидение без прокладки дополнительных проводов и наслаждаться просмотром телеканалов и видеофильмов в цифровом и HD-качестве в любом удобном месте вашей квартиры.<br><br>- Скорость до 900 Мбит/с"),

                                Map.of(
                                        "id", "6",
                                        "title", "Аренда двухдиапазонного роутера",
                                        "imageURL", "http://127.0.0.1:9000/lab1/router_rent.webp",
                                        "price", 599,
                                        "monthlyPayment", true,
                                        "unit", "шт",
                                        "description",
                                        "Роутер максимального уровня. Имеет WAN-порт 2,5Гбит/с и 3 порта 1Гбит/с для ваших устройств. Максимальная скорость Wi-Fi более 2 Гбит/с.<br><br>- Скорость до 2,5Гбит/с<br>- Поддержка 2.4 и 5 Ггц"));

        }

        public List<Map<String, ? extends Object>> getOffers() {
                return offers;
        }

        public Map<String, ? extends Object> getOfferById(String id) {
                return offers.stream()
                                .filter(offer -> offer.get("id").equals(id))
                                .findFirst().orElse(null);
        }

        public List<Map<String, ? extends Object>> searchOffers(String keyword) {
                return offers.stream()
                                .filter(offer -> ((String) offer.get("title")).toLowerCase().contains(keyword.toLowerCase()))
                                .collect(Collectors.toList());
        }
}



                // offers = List.of(
                // new Offer(
                // "1", "Облачное видеонаблюдение",
                // "http://127.0.0.1:9000/lab1/video-monitoring.webp",
                // "Система видеонаблюдения помогает предотвращать преступления, контролировать
                // сотрудников и отслеживать поток посетителей.",
                // 1129, LocalDateTime.now(), true,
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/expire-date.png",
                // "видеозаписи хранятся 14 дней"),
                // new Attribute("http://127.0.0.1:9000/lab1/notification.png",
                // "уведомления о движениях и звуках"))),
                // new Offer(
                // "2", "Виртуальная АТС", "http://127.0.0.1:9000/lab1/virtual-ats.webp",
                // "Виртуальная АТС даёт возможность обрабатывать до 100 входящих вызовов
                // одновременно, настроить голосовое приветствие и умное распределение вызовов
                // между отделами, сотрудниками или регионами.",
                // 550, LocalDateTime.now(), true,
                // // new Attribute("http://127.0.0.1:9000/lab1/phone.png", "количество номеров
                // -
                // // 3"),
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/group.png", "10 пользователей"),
                // new Attribute("http://127.0.0.1:9000/lab1/phone-call.png",
                // "до 20 входящих звонков одновременно"),
                // new Attribute("http://127.0.0.1:9000/lab1/stats.png",
                // "статистика по принятым и пропущенным звонкам"))),
                // new Offer(
                // "3", "Прокладка интернет-кабеля",
                // "http://127.0.0.1:9000/lab1/beeline-cable.jpg",
                // "Наши специалисты проведут кабель от точки входа в дом до вашего устройства и
                // настроят интернет-соединение, чтобы вы могли пользоваться всеми
                // преимуществами скоростного интернета.",
                // 0, LocalDateTime.now(), false,
                // // new Attribute("http://127.0.0.1:9000/lab1/cable-length.png", "длина кабеля
                // в
                // // метрах - 10"),
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/connectivity-type.png",
                // "тип подлключения FTTB"),
                // new Attribute("http://127.0.0.1:9000/lab1/fiber.png", "оптоволоконный
                // кабель"))),
                // new Offer(
                // "4", "Подключение статического IP-адреса",
                // "http://127.0.0.1:9000/lab1/static-ip.webp",
                // "Постоянный IP-адрес с уникальным идентификатором, который определяется
                // глобально во всей сети.",
                // 150, LocalDateTime.now(), true,
                // // new Attribute("http://127.0.0.1:9000/lab1/quantity.png", "количество
                // // IP-адресов - 2")
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/ip-protocol.png", "ipv4"))),
                // new Offer(
                // "5", "Установка и подключение PLC-адаптера",
                // "http://127.0.0.1:9000/lab1/ip-tv.webp",
                // "PLC-адаптеры – оптимальное решение, которое позволит подключить Домашнее
                // телевидение без прокладки дополнительных проводов и наслаждаться просмотром
                // телеканалов и видеофильмов в цифровом и HD-качестве в любом удобном месте
                // вашей квартиры.",
                // 3899, LocalDateTime.now(), false,
                // // new Attribute("http://127.0.0.1:9000/lab1/quantity.png", "количество
                // // адаптеров - 3")
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/wifi-speed.png", "до 900
                // Мбит/с"))),
                // new Offer(
                // "6", "Аренда двухдиапазонного роутера",
                // "http://127.0.0.1:9000/lab1/router_rent.webp",
                // "Роутер максимального уровня. Имеет WAN-порт 2,5Гбит/с и 3 порта 1Гбит/с для
                // ваших устройств. Максимальная скорость Wi-Fi более 2 Гбит/с.",
                // 599, LocalDateTime.now(), true,
                // // new Attribute("http://127.0.0.1:9000/lab1/quantity.png", "количество
                // роутеров
                // // - 2")
                // List.of(
                // new Attribute("http://127.0.0.1:9000/lab1/wifi.png", "до 500 Мбит/с"),
                // new Attribute("http://127.0.0.1:9000/lab1/frequency.png", "2,4 и 5 Ггц"))));