package me.eccentric_nz.gamemodeinventories;

public class Version implements Comparable<Version> {
   private final String version;

   public Version(String version) {
      if (version == null) {
         throw new IllegalArgumentException("Version can not be null");
      } else if (!version.matches("[0-9]+(\\.[0-9]+)*")) {
         throw new IllegalArgumentException("Invalid version format");
      } else {
         this.version = version;
      }
   }

   public String get() {
      return this.version;
   }

   public int compareTo(Version that) {
      if (that == null) {
         return 1;
      } else {
         String[] thisParts = this.get().split("\\.");
         String[] thatParts = that.get().split("\\.");
         int length = Math.max(thisParts.length, thatParts.length);

         for(int i = 0; i < length; ++i) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart) {
               return -1;
            }

            if (thisPart > thatPart) {
               return 1;
            }
         }

         return 0;
      }
   }

   public int hashCode() {
      int hash = 3;
      return hash;
   }

   public boolean equals(Object that) {
      if (this == that) {
         return true;
      } else if (that == null) {
         return false;
      } else if (this.getClass() != that.getClass()) {
         return false;
      } else {
         return this.compareTo((Version)that) == 0;
      }
   }
}
