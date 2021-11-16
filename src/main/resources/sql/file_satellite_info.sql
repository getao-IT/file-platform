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

 Date: 16/11/2021 15:32:02
*/
CREATE SEQUENCE file_satellite_info_id_seq INCREMENT BY 1 START WITH 1 MAXVALUE 99999999;

-- ----------------------------
-- Table structure for file_satellite_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_satellite_info";
CREATE TABLE "public"."file_satellite_info" (
  "id" int4 NOT NULL DEFAULT nextval('file_satellite_info_id_seq'::regclass),
  "satellite_name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "satellite_no" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6),
  "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."file_satellite_info"."satellite_no" IS '卫星编号';

-- ----------------------------
-- Records of file_satellite_info
-- ----------------------------

-- ----------------------------
-- Uniques structure for table file_satellite_info
-- ----------------------------
ALTER TABLE "public"."file_satellite_info" ADD CONSTRAINT "file_satellite_info_satellite_no_key" UNIQUE ("satellite_no");

-- ----------------------------
-- Primary Key structure for table file_satellite_info
-- ----------------------------
ALTER TABLE "public"."file_satellite_info" ADD CONSTRAINT "file_satellite_info_pkey" PRIMARY KEY ("id");
