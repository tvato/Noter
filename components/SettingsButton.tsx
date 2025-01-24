import { RootStackParamList } from "@/constants/types";
import Setting from "@/db_tools/Setting";
import { useNavigation } from "@react-navigation/native";
import { NativeStackNavigationProp } from "@react-navigation/native-stack";
import { Image, Pressable, StyleSheet } from "react-native";

type SettingsProps = NativeStackNavigationProp<RootStackParamList, "Settings">;

type Props = {
    settings: Setting;
}

export default function SettingsButton({settings}: Props){
    const navigation = useNavigation<SettingsProps>();
    return (
        <Pressable onPress={() => navigation.navigate("Settings", {settings})}>
            <Image
                source={settings.useBlackImage() ? require("@/assets/plus_black.png") : require("@/assets/plus_white.png")}
                style={styles.image}
            />
        </Pressable>
    )
}

const styles = StyleSheet.create({
    image: {
        width: 20,
        height: 20,
    }
})