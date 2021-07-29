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

 Date: 29/07/2021 11:29:16
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
INSERT INTO "public"."user_conf" VALUES ('admin', 'avatar', 'static/img/avatar.png');
INSERT INTO "public"."user_conf" VALUES ('halley.fang', 'isV', 'v1');
INSERT INTO "public"."user_conf" VALUES ('halley.fang', 'avatar', 'static/img/f.png');
INSERT INTO "public"."user_conf" VALUES ('nina.ding', 'isV', 'v2');
INSERT INTO "public"."user_conf" VALUES ('nina.ding', 'avatar', 'static/img/d.png');
INSERT INTO "public"."user_conf" VALUES ('irene.chang', 'avatar', 'static/img/c.png');
INSERT INTO "public"."user_conf" VALUES ('may.wang', 'isV', 'v2');
INSERT INTO "public"."user_conf" VALUES ('may.wang', 'avatar', 'static/img/w.png');
INSERT INTO "public"."user_conf" VALUES ('irene.chang', 'isV', 'v2');
INSERT INTO "public"."user_conf" VALUES ('admin', 'isV', 'v2');
