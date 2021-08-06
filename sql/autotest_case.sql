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

 Date: 06/08/2021 18:11:38
*/


-- ----------------------------
-- Table structure for autotest_case
-- ----------------------------
DROP TABLE IF EXISTS "public"."autotest_case";
CREATE TABLE "public"."autotest_case" (
  "case_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int4,
  "content" text COLLATE "pg_catalog"."default",
  "create_user" varchar(255) COLLATE "pg_catalog"."default",
  "create_date" date,
  "update_user" varchar(255) COLLATE "pg_catalog"."default",
  "update_date" date
)
;

-- ----------------------------
-- Primary Key structure for table autotest_case
-- ----------------------------
ALTER TABLE "public"."autotest_case" ADD CONSTRAINT "autotest_case_pkey" PRIMARY KEY ("case_id");
