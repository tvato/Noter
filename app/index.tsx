import NoteView from '@/screens/NoteView';
import HomeScreen from "@/screens/HomeScreen"
import { createStackNavigator } from "@react-navigation/stack"
import { StyleSheet } from 'react-native';
import { RootStackParamList } from '@/constants/types';
import SettingsScreen from '@/screens/SettingsScreen';
import SettingsButton from '@/components/SettingsButton';
import { useState } from 'react';
import Setting from '@/db_tools/Setting';
import DeleteNoteButton from '@/components/DeleteNoteButton';

const Stack = createStackNavigator<RootStackParamList>();

export default function AppNavigator(){
    const [settings, setSettings] = useState(new Setting());

    return (
        <Stack.Navigator initialRouteName='Home' >
            <Stack.Screen
                name="Home"
                component={HomeScreen}
                options={{headerRight: () => <SettingsButton settings={settings} />}}
                initialParams={{settings}}
            />
            <Stack.Screen
                name="NoteView"
                component={NoteView}
                options={{headerRight: () => <DeleteNoteButton />}}
            />
            <Stack.Screen
                name="Settings"
                component={SettingsScreen}
            />
        </Stack.Navigator>
    );
}

const styles = StyleSheet.create({});