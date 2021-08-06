/*
 Navicat Premium Data Transfer

 Source Server         : 25pg
 Source Server Type    : PostgreSQL
 Source Server Version : 90420
 Source Host           : 192.168.0.25:5432
 Source Catalog        : testm
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90420
 File Encoding         : 65001

 Date: 03/08/2021 14:52:26
*/


-- ----------------------------
-- Table structure for case_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."case_info";
CREATE TABLE "public"."case_info" (
  "id" int8,
  "case_id" varchar COLLATE "pg_catalog"."default",
  "case_name" varchar COLLATE "pg_catalog"."default",
  "case_pre" varchar COLLATE "pg_catalog"."default",
  "case_step" varchar COLLATE "pg_catalog"."default",
  "case_post" varchar COLLATE "pg_catalog"."default",
  "remark" varchar COLLATE "pg_catalog"."default",
  "is_delete" bool,
  "is_reviewed" bool,
  "create_user" varchar COLLATE "pg_catalog"."default",
  "create_date" timestamp(6),
  "update_user" varchar COLLATE "pg_catalog"."default",
  "update_date" timestamp(6),
  "issue_id" varchar COLLATE "pg_catalog"."default",
  "is_v" varchar COLLATE "pg_catalog"."default",
  "is_auto" bool
)
;
