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

 Date: 06/08/2021 18:11:24
*/


-- ----------------------------
-- Table structure for autotest_case_exec
-- ----------------------------
DROP TABLE IF EXISTS "public"."autotest_case_exec";
CREATE TABLE "public"."autotest_case_exec" (
  "exec_id" int8 NOT NULL,
  "case_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int4 NOT NULL,
  "start_date" timestamp(0),
  "end_date" timestamp(0),
  "current" int8,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "exec_retry_count" int4,
  "exec_parameter" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "update_date" timestamp(6)
)
;

-- ----------------------------
-- Primary Key structure for table autotest_case_exec
-- ----------------------------
ALTER TABLE "public"."autotest_case_exec" ADD CONSTRAINT "autotest_case_exec_pkey" PRIMARY KEY ("exec_id", "case_id", "exec_parameter");
