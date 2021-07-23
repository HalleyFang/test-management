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

 Date: 23/07/2021 17:54:29
*/


-- ----------------------------
-- Table structure for user_conf
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_conf";
CREATE TABLE "public"."user_conf" (
  "username" varchar COLLATE "pg_catalog"."default",
  "parameter" varchar COLLATE "pg_catalog"."default",
  "value" varchar COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of user_conf
-- ----------------------------
INSERT INTO "public"."user_conf" VALUES ('admin', 'isV', 'v1');
