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

 Date: 23/07/2021 17:53:05
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
  "create_time" timestamp(6),
  "update_user" varchar COLLATE "pg_catalog"."default",
  "update_time" timestamp(6),
  "issue_id" varchar COLLATE "pg_catalog"."default",
  "is_v" varchar COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of case_info
-- ----------------------------
