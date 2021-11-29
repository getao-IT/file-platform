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

 Date: 29/11/2021 15:24:21
*/

CREATE SEQUENCE file_transfer_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_transfer_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_transfer_info";
CREATE TABLE "public"."file_transfer_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_transfer_info_id_seq'::regclass),
  "file_save_dir" varchar(255) COLLATE "pg_catalog"."default",
  "is_public" bool,
  "keywords" varchar(255) COLLATE "pg_catalog"."default",
  "source" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" int4,
  "batch_number" int4,
  "create_time" timestamp(0),
  "user_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_type" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table file_transfer_info
-- ----------------------------
ALTER TABLE "public"."file_transfer_info" ADD CONSTRAINT "image_tile_upload_info_copy1_pkey" PRIMARY KEY ("id");
