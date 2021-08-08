/*
 Navicat Premium Data Transfer

 Source Server         : postgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 130003
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130003
 File Encoding         : 65001

 Date: 08/08/2021 21:13:12
*/


-- ----------------------------
-- Table structure for scatter_chart
-- ----------------------------
DROP TABLE IF EXISTS "public"."scatter_chart";
CREATE TABLE "public"."scatter_chart" (
  "exec_id" int8 NOT NULL,
  "case_id" varchar COLLATE "pg_catalog"."default" NOT NULL,
  "exec_parameter" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "exec_time" int8,
  "failed_rate" float8,
  "exec_total" int4,
  "failed_count" int4,
  "is_v" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table scatter_chart
-- ----------------------------
ALTER TABLE "public"."scatter_chart" ADD CONSTRAINT "scatter_chart_pkey" PRIMARY KEY ("exec_id", "case_id", "exec_parameter");
