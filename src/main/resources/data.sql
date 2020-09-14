INSERT INTO USER(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY, EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (1, 'kkaniap', 'kania123', 'Patryk', 'Kania', true, '1996-03-02', 'xkaniax@gmail.com', '+48531281648', 'Andersa 20/9',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-02-01 10:10:00');
INSERT INTO USER(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY,EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (2, 'prietza', 'adrian123', 'Adrian', 'Prietz', true, '1996-10-11', 'adrian.prietz@gmail.com', '+48562333145', 'Malinowa 22',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-02-01 11:15:00');
INSERT INTO USER(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY,EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (3, 'karnow', 'karolina11', 'Karolina', 'Nowak', false, '1999-02-21', 'karolina.nowak@gmail.com', '+48568789885', 'Asnyka 2/22',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-05-11 02:44:43');

INSERT INTO USER_ROLE(ID, ROLE) VALUES (1, 'ROLE_ADMIN');
INSERT INTO USER_ROLE(ID, ROLE) VALUES (2, 'ROLE_USER');

INSERT INTO USER_ROLES(USER_ID, ROLES_ID) VALUES (1, 1);
INSERT INTO USER_ROLES(USER_ID, ROLES_ID) VALUES (1, 2);
INSERT INTO USER_ROLES(USER_ID, ROLES_ID) VALUES (2, 2);
INSERT INTO USER_ROLES(USER_ID, ROLES_ID) VALUES (3, 2);

INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED)
 VALUES (1, 100, 80, '06:00:00', '19:00:00', 5);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED)
 VALUES (2, 100, 80, '06:00:00', '19:00:00', 5);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED)
 VALUES (3, 80, 85, '08:30:00', '20:00:00', 5);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED)
 VALUES (4, 100, 70, '07:00:00', '20:00:00', 5);

INSERT INTO TERRARIUM (ID, USER_ID, NAME, AUTO_MANAGEMENT, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (1, 2, 'Terrarium 1', true, '2020-04-22', 1);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, AUTO_MANAGEMENT, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (2, 2, 'Terrarium 2', true, '2020-04-22', 2);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, AUTO_MANAGEMENT, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (3, 2, 'Terrarium 3', true, '2020-04-22', 3);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, AUTO_MANAGEMENT, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (4, 3, 'Terrarium 1', true, '2020-04-22', 4);

INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (1, 1, 40.2, 80, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (2, 1, 42.5, 83, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (3, 2, 40.2, 80, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (4, 2, 42.5, 83, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (5, 3, 40.2, 80, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (6, 3, 42.5, 83, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (7, 4, 40.2, 80, 80, 230, 450, 80, '2020-04-22 10:10:00');
INSERT INTO SENSORS_READS (ID, TERRARIUM_ID, TEMPERATURE, HUMIDITY, BRIGHTNESS, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL,
 READ_DATE)
 VALUES (8, 4, 42.5, 83, 80, 230, 450, 80, '2020-04-22 10:10:00');

INSERT INTO NEWS (ID, USER_ID, READ_TIME, CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (1, 1, 5, 'Lorem ipsum', 'img/thumb/1.jpg', 'img/thumb/1_m.jpg', 'img/news/1.jpg', 'img/news/1_m.jpg',
 '2020-04-11 10:01:23');
INSERT INTO NEWS (ID, USER_ID, READ_TIME, CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (2, 1, 15, 'Lorem ipsum', 'img/thumb/2.jpg', 'img/thumb/2_m.jpg', 'img/news/2.jpg', 'img/news/2_m.jpg',
 '2020-04-15 08:14:11');
INSERT INTO NEWS (ID, USER_ID, READ_TIME, CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (3, 1, 10, 'Lorem ipsum', 'img/thumb/3.jpg', 'img/thumb/3_m.jpg', 'img/news/3.jpg', 'img/news/3_m.jpg',
 '2020-04-11 15:01:23');

INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (1, 'LOW_WATER_LEVEL_IN_CONTAINER',
 'You need to refill the water container');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (2, 'LOW_TEMPERATURE',
 'You have to raise the temperature');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (3, 'HIGH_TEMPERATURE',
 'You have to lower the temperature');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (4, 'LOW_UVA_LEVEL',
 'You need to raise UVA level');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (5, 'LOW_UVB_LEVEL',
 'You need to raise UVB level');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (6, 'LOW_HUMIDITY_LEVEL',
 'You need to raise humidity level');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (7, 'HIGH_HUMIDITY_LEVEL',
 'You need to lower humidity level');
INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (8, 'TERRARIUM_IS_OPEN',
 'You need to close terrarium');

INSERT INTO ALERT (ID, CREATE_DATE, LEVEL, TERRARIUM_ID, TYPE_ID) VALUES (1, '2020-04-22 10:11:23', 'LOW', '1', 1);
INSERT INTO ALERT (ID, CREATE_DATE, LEVEL, TERRARIUM_ID, TYPE_ID) VALUES (2, '2020-04-22 11:11:23', 'MEDIUM', '1', 3);
INSERT INTO ALERT (ID, CREATE_DATE, LEVEL, TERRARIUM_ID, TYPE_ID) VALUES (3, '2020-04-22 10:11:23', 'LOW', '2', 1);


