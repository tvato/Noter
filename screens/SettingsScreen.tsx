import { RootStackParamList } from "@/constants/types";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Button, SafeAreaView, ScrollView, StyleSheet, Text } from "react-native";
import { Picker } from "@react-native-picker/picker";
import { useEffect, useState } from "react";
import { colorNames } from "@/constants/colorList";

type SettingsProps = NativeStackScreenProps<RootStackParamList, "Settings">;

export default function SettingsScreen({route, navigation}: SettingsProps){
    const [settings, setSettings] = useState(route.params.settings.getSettings());
    const [backgroundColor, setBackgroundColor] = useState(settings.backgroundColor);
    const [textColor, setTextColor] = useState(settings.textColor);
    const [checkedTextColor, setChekedTextColor] = useState(settings.checkedTextColor);
    const [borderColorNote, setBorderColorNote] = useState(settings.borderColorNote);
    const [borderColorNewNote, setBorderColorNewNote] = useState(settings.borderColorNewNote);
    const [backgroundColorNewNote, setBackgroundColorNewNote] = useState(settings.backgroundColorNewNote);

    const submitChange = async () => {
        const newSettings = {
            id: settings.id | 1,
            backgroundColor: backgroundColor,
            textColor: textColor,
            checkedTextColor: checkedTextColor,
            borderColorNote: borderColorNote,
            borderColorNewNote: borderColorNewNote,
            backgroundColorNewNote: backgroundColorNewNote
        }

        route.params.settings.setSettings(newSettings);
        setSettings(newSettings);
    }

    // Workaround for updating header colors
    useEffect(() => {
        navigation.setOptions(route.params.settings.getHeaderOptions())
    }, [route.params.settings.settings])

    return (
        <SafeAreaView
            style={[styles.container,{ backgroundColor: settings.backgroundColor }]}>
            <ScrollView>
                <Text style={[styles.titles, { color: settings.textColor }]} >Background Color:</Text>
                <Picker
                    selectedValue={backgroundColor}
                    onValueChange={setBackgroundColor}
                    style={{ color: settings.textColor }}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value}
                            key={value}
                        />
                    ))}
                </Picker>

                <Text style={[styles.titles, { color: settings.textColor }]} >Text Color:</Text>
                <Picker
                    selectedValue={textColor}
                    onValueChange={setTextColor}
                    style={{color: settings.textColor}}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value}
                            key={value}
                        />
                    ))}
                </Picker>

                <Text style={[styles.titles, { color: settings.textColor }]} >Checked Item Text Color:</Text>
                <Picker
                    selectedValue={checkedTextColor}
                    onValueChange={setChekedTextColor}
                    style={{color: settings.textColor}}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value}
                            key={value}
                        />
                    ))}
                </Picker>

                <Text style={[styles.titles, { color: settings.textColor }]} >Border Color (Note):</Text>
                <Picker
                    selectedValue={borderColorNote}
                    onValueChange={setBorderColorNote}
                    style={{color: settings.textColor}}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value}
                            key={value}
                        />
                    ))}
                </Picker>

                <Text style={[styles.titles, { color: settings.textColor }]} >Border Color (NewNote):</Text>
                <Picker
                    selectedValue={borderColorNewNote}
                    onValueChange={setBorderColorNewNote}
                    style={{color: settings.textColor}}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value} 
                            key={value}
                        />
                    ))}
                </Picker>

                <Text style={[styles.titles, { color: settings.textColor }]} >Background Color (NewNote):</Text>
                <Picker
                    selectedValue={backgroundColorNewNote}
                    onValueChange={setBackgroundColorNewNote}
                    style={{color: settings.textColor}}
                    dropdownIconColor={settings.textColor}
                >
                    {colorNames.map((value) => (
                        <Picker.Item
                            label={value}
                            value={value}
                            key={value}
                        />
                    ))}
                </Picker>
            </ScrollView>
            <Button title="Save" onPress={submitChange} />
        </SafeAreaView>
    )
}

const styles = StyleSheet.create({
    titles: {
        marginLeft: 10,
        fontSize: 20,
        fontWeight: "bold",
    },
    container: {
        flex: 1,
        padding: 10,
    }
})