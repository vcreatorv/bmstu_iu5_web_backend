package com.valer.rip.lab1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.valer.rip.lab1.models.ProviderDuty;
import com.valer.rip.lab1.repositories.ProviderDutyRepository;

@Service
public class ProviderDutiesService {

        private final ProviderDutyRepository providerDutiesRepository;

        public ProviderDutiesService(ProviderDutyRepository providerDutiesRepository) {
                this.providerDutiesRepository = providerDutiesRepository;
        }

        public List<ProviderDuty> getProviderDuties() {
                return providerDutiesRepository.findAll();
        }

        public Optional<ProviderDuty> getProviderDutyById(int id) {
                return Optional.ofNullable(providerDutiesRepository.findById(id));
        }

        public List<ProviderDuty> findDutyByTitle(String serviceTitle) {
                return providerDutiesRepository.findDutyByTitle(serviceTitle.toLowerCase());
            }
}


// private List<Map<String, ? extends Object>> providerDuties;

        // @PostConstruct
        // public void init() {
        //         providerDuties = List.of(
        //                         Map.of(
        //                                 "id", 1,
        //                                 "title", "Облачное видеонаблюдение",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/1.png",
        //                                 "price", 1129,
        //                                 "monthlyPayment", true,
        //                                 "unit", "шт",
        //                                 "amountDescription", "количество камер",
        //                                 "description",
        //                                 "Система видеонаблюдения помогает предотвращать преступления, контролировать сотрудников и отслеживать поток посетителей.<br><br>- Хранение записи в облаке до 14 дней <br>- Уведомления о движениях и звуках"),

        //                         Map.of(
        //                                 "id", 2,
        //                                 "title", "Виртуальная АТС",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/2.png",
        //                                 "price", 350,
        //                                 "monthlyPayment", true,
        //                                 "unit", "шт",
        //                                 "amountDescription", "количество номеров",
        //                                 "description",
        //                                 "Виртуальная АТС даёт возможность обрабатывать до 100 входящих вызовов одновременно, настроить голосовое приветствие и умное распределение вызовов между отделами, сотрудниками или регионами.<br><br>- До 20 входящих звонков одновременно<br>- Статистика по принятым и пропущенным звонкам"),

        //                         Map.of(
        //                                 "id", 3,
        //                                 "title", "Прокладка интернет-кабеля",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/3.png",
        //                                 "price", 500,
        //                                 "monthlyPayment", false,
        //                                 "unit", "метр",
        //                                 "amountDescription", "длина кабеля в метрах",
        //                                 "description",
        //                                 "Наши специалисты проведут кабель от точки входа в дом до вашего устройства и настроят интернет-соединение, чтобы вы могли пользоваться всеми преимуществами скоростного интернета.<br><br>- Тип подлключения FTTB<br>- Оптоволоконный материал"),

        //                         Map.of(
        //                                 "id", 4,
        //                                 "title", "Подключение статического IP-адреса",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/4.png",
        //                                 "price", 150,
        //                                 "monthlyPayment", true,
        //                                 "unit", "шт",
        //                                 "amountDescription", "количество IP-адресов",
        //                                 "description",
        //                                 "Постоянный IP-адрес с уникальным идентификатором, который определяется глобально во всей сети.<br><br>- Интернет-протокол ipv4"),

        //                         Map.of(
        //                                 "id", 5,
        //                                 "title", "Установка и подключение PLC-адаптера",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/5.png",
        //                                 "price", 2899,
        //                                 "monthlyPayment", false,
        //                                 "unit", "шт",
        //                                 "amountDescription", "количество PLC-адаптеров",
        //                                 "description",
        //                                 "PLC-адаптеры – оптимальное решение, которое позволит подключить Домашнее телевидение без прокладки дополнительных проводов и наслаждаться просмотром телеканалов и видеофильмов в цифровом и HD-качестве в любом удобном месте вашей квартиры.<br><br>- Скорость до 900 Мбит/с"),

        //                         Map.of(
        //                                 "id", 6,
        //                                 "title", "Аренда двухдиапазонного роутера",
        //                                 "imageURL", "http://127.0.0.1:9000/lab1/6.png",
        //                                 "price", 599,
        //                                 "monthlyPayment", true,
        //                                 "unit", "шт",
        //                                 "amountDescription", "количество роутеров",
        //                                 "description",
        //                                 "Роутер максимального уровня. Имеет WAN-порт 2,5Гбит/с и 3 порта 1Гбит/с для ваших устройств. Максимальная скорость Wi-Fi более 2 Гбит/с.<br><br>- Скорость до 2,5Гбит/с<br>- Поддержка 2.4 и 5 Ггц"));

        // }

        // public List<Map<String, ? extends Object>> getProviderDuties() {
        //         return providerDuties;
        // }

        // public Map<String, ? extends Object> getProviderDutyById(String id) {
        //         return providerDuties.stream()
        //                         .filter(duty -> duty.get("id").equals(id))
        //                         .findFirst().orElse(null);
        // }

        // public List<Map<String, ? extends Object>> searchProviderDuties(String serviceTitle) {
        //         return providerDuties.stream()
        //                         .filter(duty -> ((String) duty.get("title")).toLowerCase().contains(serviceTitle.toLowerCase()))
        //                         .collect(Collectors.toList());
        // }