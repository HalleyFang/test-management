/*
 Navicat Premium Data Transfer

 Source Server         : local pg
 Source Server Type    : PostgreSQL
 Source Server Version : 100009
 Source Host           : 192.168.31.158:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100009
 File Encoding         : 65001

 Date: 26/07/2021 19:26:35
*/


-- ----------------------------
-- Table structure for task_case
-- ----------------------------
DROP TABLE IF EXISTS "public"."task_case";
CREATE TABLE "public"."task_case" (
  "case_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "task_id" int4 NOT NULL,
  "case_status" int4,
  "update_user" varchar(255) COLLATE "pg_catalog"."default",
  "update_date" timestamp(6),
  "tree_id" int4
)
;

-- ----------------------------
-- Primary Key structure for table task_case
-- ----------------------------
ALTER TABLE "public"."task_case" ADD CONSTRAINT "task_case_pkey" PRIMARY KEY ("case_id", "task_id");
