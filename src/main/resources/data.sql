INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY, EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (1, 'kkaniap', 'kania123', 'Patryk', 'Kania', true, '1996-03-02', 'xkaniax@gmail.com', '+48531281648', 'Andersa 20/9',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-02-01 10:10:00');
INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY,EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (2, 'prietza', 'adrian123', 'Adrian', 'Prietz', true, '1996-10-11', 'adrian.prietz@gmail.com', '+48562333145', 'Malinowa 22',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-02-01 11:15:00');
INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY,EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (3, 'karnow', 'karolina11', 'Karolina', 'Nowak', false, '1999-02-21', 'karolina.nowak@gmail.com', '+48568789885', 'Asnyka 2/22',
 'Tychy', 'Slask', '43-100', 'Poland', true, '2020-05-11 02:44:43');
 INSERT INTO USERS(ID, USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, IS_MEN, BIRTHDAY,EMAIL, PHONE, ADDRESS, CITY, STATE,
 POST_CODE,COUNTRY, IS_ACTIVE, ACCOUNT_CREATED)
 VALUES (4, 'test', 'test', 'tes', 'test', false, '1999-02-21', 'test@gmail.com', 'test', 'test',
 'test', 'test', 'test', 'test', true, '2020-05-11 02:44:43');

INSERT INTO SENSORS_READS(ID, HUMIDITY, IS_OPEN, READ_DATE, TEMPERATURE, UVA_LEVEL, UVB_LEVEL, WATER_LEVEL)
 VALUES(1, 50, false, '2020-02-01 11:15:00', 25.5, 50.0, 40.0, 40);

INSERT INTO USER_ROLE(ID, ROLE) VALUES (1, 'ROLE_ADMIN');
INSERT INTO USER_ROLE(ID, ROLE) VALUES (2, 'ROLE_USER');

INSERT INTO USERS_ROLES(USER_ID, ROLES_ID) VALUES (1, 1);
INSERT INTO USERS_ROLES(USER_ID, ROLES_ID) VALUES (1, 2);
INSERT INTO USERS_ROLES(USER_ID, ROLES_ID) VALUES (2, 2);
INSERT INTO USERS_ROLES(USER_ID, ROLES_ID) VALUES (3, 2);
INSERT INTO USERS_ROLES(USER_ID, ROLES_ID) VALUES (4, 2);

INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, WATER_LEVEL,SUNRISE_TIME, SUNSET_TIME, SUN_SPEED, IS_BULB_WORKING, IS_HUMIDIFIER_WORKING, AUTO_MANAGEMENT)
 VALUES (1, 100, 80, 100,'06:10:00', '19:00:00', 5, false, false, true);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, WATER_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED, IS_BULB_WORKING, IS_HUMIDIFIER_WORKING, AUTO_MANAGEMENT)
 VALUES (2, 100, 80, 100, '06:00:00', '19:00:00', 5, false, false, true);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, WATER_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED, IS_BULB_WORKING, IS_HUMIDIFIER_WORKING, AUTO_MANAGEMENT)
 VALUES (3, 80, 85, 100, '08:30:00', '20:00:00', 5, false, false, true);
INSERT INTO TERRARIUM_SETTINGS(ID, LIGHT_POWER, HUMIDITY_LEVEL, WATER_LEVEL, SUNRISE_TIME, SUNSET_TIME, SUN_SPEED, IS_BULB_WORKING, IS_HUMIDIFIER_WORKING, AUTO_MANAGEMENT)
 VALUES (4, 100, 70, 100, '07:00:00', '20:00:00', 5, false, false, true);

INSERT INTO TERRARIUM (ID, USER_ID, NAME, CREATE_DATE, TERRARIUM_SETTINGS_ID, SENSORS_READS_ID)
 VALUES (1, 1, 'Terrarium_test','2020-04-22', 1, 1);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (2, 2, 'Terrarium 2','2020-04-22', 2);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (3, 2, 'Terrarium 3','2020-04-22', 3);
INSERT INTO TERRARIUM (ID, USER_ID, NAME, CREATE_DATE, TERRARIUM_SETTINGS_ID)
 VALUES (4, 3, 'Terrarium 1','2020-04-22', 4);

INSERT INTO NEWS (ID, USER_ID, TITLE, READ_TIME, SHORT_CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (1, 1, 'My Bachelor thesis', 5, 'Where did the idea come from ?<br>How does it look like at my university ?<br/>And whats next ?<br><br>Answers to these questions and more are in the post', '../../../assets/homeAssets/NewsID/1/logo_1.png',
  '../../../assets/homeAssets/NewsID/1/logo_mobile_1.png', '../../../assets/newsAssets/NewsID/1/logo_1.png', '../../../assets/homeAssets/NewsID/1/logo_mobile_1.png',
 '2020-04-11 10:01:23');
INSERT INTO NEWS (ID, USER_ID, TITLE, READ_TIME, SHORT_CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (2, 1, 'What I am working on', 15, 'You will find details about the module plans that you can install to your terrarium and what changes I plan on the website', '../../../assets/homeAssets/NewsID/2/logo_2.png',
  '../../../assets/homeAssets/NewsID/2/logo_mobile_2.png', '../../../assets/newsAssets/NewsID/2/logo_2.png', '../../../assets/homeAssets/NewsID/2/logo_mobile_2.png',
 '2020-04-15 08:14:11');
INSERT INTO NEWS (ID, USER_ID, TITLE, READ_TIME, SHORT_CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (3, 1, 'How to save money with TerraSI', 1, '  Do you want to know how to save money by buying more ? You can get up to -25% discount. Just visit this post.', '../../../assets/homeAssets/NewsID/3/logo_3.png',
  '../../../assets/homeAssets/NewsID/3/logo_mobile_3.png', '../../../assets/newsAssets/NewsID/3/logo_3.png', '../../../assets/homeAssets/NewsID/3/logo_mobile_3.png',
 '2020-04-11 15:01:23');
 INSERT INTO NEWS (ID, USER_ID, TITLE, READ_TIME, SHORT_CONTENT, IMG_THUMBNAIL, IMG_THUMBNAIL_MOBILE, IMG_NEWS, IMG_NEWS_MOBILE,
 CREATE_DATE)
 VALUES (4, 1, 'We have started working together', 5, 'We are pleased to inform you that we have started cooperation with ReptileSupply. This will contribute to better quality of our terrariums.If you want to know more about our cooperation, please visit this post.', '../../../assets/homeAssets/NewsID/4/logo_4.png',
  '../../../assets/homeAssets/NewsID/4/logo_mobile_4.png', '../../../assets/newsAssets/NewsID/4/logo_4.png', '../../../assets/homeAssets/NewsID/4/logo_mobile_4.png',
 '2020-04-11 15:01:23');

INSERT INTO ALERT_TYPE (ID, TYPE, DESCRIPTION) VALUES (1, 'LOW_WATER_IN_CONTAINER',
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


