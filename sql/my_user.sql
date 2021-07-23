/*
 Navicat Premium Data Transfer

 Source Server         : local pg
 Source Server Type    : PostgreSQL
 Source Server Version : 90506
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90506
 File Encoding         : 65001

 Date: 23/07/2021 17:54:05
*/


-- ----------------------------
-- Table structure for my_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."my_user";
CREATE TABLE "public"."my_user" (
  "id" int4,
  "username" varchar COLLATE "pg_catalog"."default",
  "password" varchar COLLATE "pg_catalog"."default",
  "role" varchar COLLATE "pg_catalog"."default",
  "permissions" varchar COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of my_user
-- ----------------------------
INSERT INTO "public"."my_user" VALUES (1, 'admin', '123456', NULL, NULL);
