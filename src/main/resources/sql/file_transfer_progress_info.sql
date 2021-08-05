/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.30.25
 Source Server Type    : PostgreSQL
 Source Server Version : 90515
 Source Host           : 192.168.30.25:31189
 Source Catalog        : geodl_iecas
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 90515
 File Encoding         : 65001

 Date: 02/08/2021 10:43:03
*/

CREATE SEQUENCE file_transfer_progress_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_transfer_progress_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_transfer_progress_info";
CREATE TABLE "public"."file_transfer_progress_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_transfer_progress_info_id_seq'::regclass),
  "md5" varchar(255) COLLATE "pg_catalog"."default",
  "chunks" int4,
  "transferred_chunk" int4,
  "create_time" timestamp(6),
  "file_transfer_id" int4
)
;
COMMENT ON COLUMN "public"."file_transfer_progress_info"."chunks" IS '总分块数';
COMMENT ON COLUMN "public"."file_transfer_progress_info"."transferred_chunk" IS '上传分片数';
COMMENT ON COLUMN "public"."file_transfer_progress_info"."file_transfer_id" IS '文件传输信息id';

-- ----------------------------
-- Primary Key structure for table file_transfer_progress_info
-- ----------------------------
ALTER TABLE "public"."file_transfer_progress_info" ADD CONSTRAINT "image_upload_info_copy1_pkey" PRIMARY KEY ("id");
