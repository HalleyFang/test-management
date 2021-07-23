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

 Date: 23/07/2021 17:54:19
*/


-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS "public"."sequence";
CREATE TABLE "public"."sequence" (
  "name" varchar COLLATE "pg_catalog"."default",
  "id" int4
)
;

-- ----------------------------
-- Records of sequence
-- ----------------------------
INSERT INTO "public"."sequence" VALUES ('caseInfo', 9);
INSERT INTO "public"."sequence" VALUES ('caseId', 32);
INSERT INTO "public"."sequence" VALUES ('treeId', 51);
