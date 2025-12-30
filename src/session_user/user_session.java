package session_user;


import java.util.List;

public class user_session {

    private static Integer idPersonal;
    private static String username;
    private static String namaPersonal;
    private static List<Integer> idPeranList;
    private static List<String> peranList;

    // Getter dan Setter
    public static Integer getIdPersonal() {
        return idPersonal;
    }

    public static void setIdPersonal(Integer idPersonal) {
        user_session.idPersonal = idPersonal;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        user_session.username = username;
    }

    public static String getNamaPersonal() {
        return namaPersonal;
    }

    public static void setNamaPersonal(String namaPersonal) {
        user_session.namaPersonal = namaPersonal;
    }

    public static List<Integer> getIdPeranList() {
        return idPeranList;
    }

    public static void setIdPeranList(List<Integer> idPeranList) {
        user_session.idPeranList = idPeranList;
    }

    public static List<String> getPeranList() {
        return peranList;
    }

    public static void setPeranList(List<String> peranList) {
        user_session.peranList = peranList;
    }

    // Method untuk cek apakah user punya peran tertentu
    public static boolean hasRole(String roleName) {
        return peranList != null && peranList.contains(roleName);
    }

    public static boolean hasRoleId(Integer roleId) {
        return idPeranList != null && idPeranList.contains(roleId);
    }

    // Clear session saat logout
    public static void clearSession() {
        idPersonal = null;
        username = null;
        namaPersonal = null;
        idPeranList = null;
        peranList = null;
    }
}
