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

 Date: 25/07/2021 17:03:59
*/


-- ----------------------------
-- Table structure for tasks
-- ----------------------------
DROP TABLE IF EXISTS "public"."tasks";
CREATE TABLE "public"."tasks" (
  "id" int4 NOT NULL,
  "label" varchar(255) COLLATE "pg_catalog"."default",
  "status" int4,
  "executor" varchar(255) COLLATE "pg_catalog"."default",
  "start_date" date,
  "end_date" date,
  "create_user" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(0),
  "update_user" varchar(255) COLLATE "pg_catalog"."default",
  "update_time" timestamp(0),
  "is_v" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table tasks
-- ----------------------------
ALTER TABLE "public"."tasks" ADD CONSTRAINT "tasks_pkey" PRIMARY KEY ("id");
