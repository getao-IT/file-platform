/*
 Navicat Premium Data Transfer

 Source Server         : 9.64
 Source Server Type    : PostgreSQL
 Source Server Version : 120003
 Source Host           : 192.168.9.64:32189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120003
 File Encoding         : 65001

 Date: 20/04/2022 10:21:21
*/


-- ----------------------------
-- Table structure for file_elec_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_elec_info";
CREATE TABLE "public"."file_elec_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_elec_info_id_seq'::regclass),
  "user_id" int4,
  "is_public" bool,
  "elec_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "source" varchar(255) COLLATE "pg_catalog"."default",
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "keywords" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "size" varchar(50) COLLATE "pg_catalog"."default",
  "batch_number" int4,
  "count" int4
)
;
COMMENT ON COLUMN "public"."file_elec_info"."id" IS 'id';
COMMENT ON COLUMN "public"."file_elec_info"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."file_elec_info"."elec_name" IS '影像名称';
COMMENT ON COLUMN "public"."file_elec_info"."path" IS '存放路径';
COMMENT ON COLUMN "public"."file_elec_info"."source" IS '来源';
COMMENT ON COLUMN "public"."file_elec_info"."user_name" IS '用户名称';
COMMENT ON COLUMN "public"."file_elec_info"."keywords" IS '标签';
COMMENT ON COLUMN "public"."file_elec_info"."batch_number" IS '上传批次';

-- ----------------------------
-- Primary Key structure for table file_elec_info
-- ----------------------------
ALTER TABLE "public"."file_elec_info" ADD CONSTRAINT "file_elec_info_pkey" PRIMARY KEY ("id");
