DROP DATABASE IF EXISTS blogapp;
CREATE DATABASE blogapp;
USE blogapp;

CREATE TABLE users(
    id int PRIMARY KEY AUTO_INCREMENT , 
    firstname varchar(100) NOT NULL ,
    lastname varchar(100), 
    username varchar(75) NOT NULL, 
    bio varchar(1000), 
    email varchar(255) UNIQUE NOT NULL, 
    passwd varchar(255) NOT NULL
);

CREATE TABLE user_followings(
    followeeId int NOT NULL,
    followerId int NOT NULL,
    PRIMARY KEY(followerId, followeeId),

    CONSTRAINT fk_follower FOREIGN KEY (followerId) REFERENCES users(id) ON DELETE CASCADE, 
    CONSTRAINT fk_followee FOREIGN KEY (followeeId) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE articles(

    id int PRIMARY KEY AUTO_INCREMENT,
    author_id int NOT NULL,
    title varchar(255) UNIQUE NOT NULL ,
    article_description varchar(255) NOT NULL,
    body varchar NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tags(

    id int PRIMARY KEY AUTO_INCREMENT,
    tag varchar(30) UNIQUE NOT NULL
);

CREATE TABLE articles_tags(
    
    article_id int NOT NULL,
    tag_id int NOT NULL,

    PRIMARY KEY(article_id, tag_id),
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag   FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE

);

CREATE TABLE article_favourites(
    
    article_id int NOT NULL,
    user_id int NOT NULL,
    
    PRIMARY KEY (article_id , user_id),
    CONSTRAINT fk_article_fav FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE ,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE comments(
    
    id int PRIMARY KEY AUTO_INCREMENT,
    body varchar(700) NOT NULL,
    user_id int NOT NULL,
    article_id int NOT NULL,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

