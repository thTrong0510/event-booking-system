/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.nvtt.pojo.dtos.event;

/**
*
* @author lequa
*/
public class ResEventMediaDTO {
   private String mediaType;
   private String mediaUrl;

   public ResEventMediaDTO(String mediaType, String mediaUrl) {
       this.mediaType = mediaType;
       this.mediaUrl = mediaUrl;
   }

   /**
    * @return the mediaType
    */
   public String getMediaType() {
       return mediaType;
   }

   /**
    * @param mediaType the mediaType to set
    */
   public void setMediaType(String mediaType) {
       this.mediaType = mediaType;
   }

   /**
    * @return the mediaUrl
    */
   public String getMediaUrl() {
       return mediaUrl;
   }

   /**
    * @param mediaUrl the mediaUrl to set
    */
   public void setMediaUrl(String mediaUrl) {
       this.mediaUrl = mediaUrl;
   }
   
   
}
