import { colorHex } from "@/constants/colorList";
import { addToSettings, createTables, getAllSettings, getDBConnection, updateSettings } from "./notes";
import { HeaderOptions, Settings } from "@/constants/types";


export default class Setting{
    settings: Settings = {
        id: 0,
        backgroundColor: "black",
        textColor: "white",
        checkedTextColor: "darkgrey",
        borderColorNote: "grey",
        borderColorNewNote: "darkgrey",
        backgroundColorNewNote: "grey",
    };
    headerOptions: HeaderOptions = {
        headerStyle: { backgroundColor: "black" },
        headerTintColor: "white",
        headerTitle: "" // Never
    }
    constructor(){
        const retrieveSettings = async () => {
            const db = await getDBConnection();
            const results = await getAllSettings(db);
            if(results){
                this.settings = results;

                this.headerOptions = {
                    headerStyle : { backgroundColor: results.backgroundColor },
                    headerTintColor: results.textColor,
                    headerTitle: ""
                }
            }
        }

        const makeTables = async () => {
            const db = await getDBConnection();
            await createTables(db);
            await addToSettings(db, this.settings)
        }
        
        makeTables();
        retrieveSettings();
    };

    getSettings(){
        return this.settings;
    }

    getHeaderOptions(){
        return this.headerOptions
    }

    setSettings(newSettings: Settings){
        const changeSettings = async () => {
            const db = await getDBConnection();
            await updateSettings(db, newSettings)
        }
        changeSettings()
        this.settings = newSettings;
        this.headerOptions = {
            headerStyle : { backgroundColor: newSettings.backgroundColor },
            headerTintColor: newSettings.textColor,
            headerTitle: ""
        }
    }

    useBlackImage(){
        const color = this.settings.backgroundColor
        const colorTyped = color as keyof typeof colorHex;
        const rgbColor = colorHex[colorTyped].slice(1).match(/.{2}/g)?.map((hex) => parseInt(hex, 16))

        const [r, g, b] = rgbColor ? rgbColor : [0,0,0]

        const brightness = (0.2126 * r) + (0.7152 * g) + (0.0722 * b);

        return brightness > 186 ? true : false;
    }

    useBlackImageNewNote(){
        const color = this.settings.backgroundColorNewNote
        const colorTyped = color as keyof typeof colorHex;
        const rgbColor = colorHex[colorTyped].slice(1).match(/.{2}/g)?.map((hex) => parseInt(hex, 16))

        const [r, g, b] = rgbColor ? rgbColor : [0,0,0]

        const brightness = (0.2126 * r) + (0.7152 * g) + (0.0722 * b);

        return brightness > 50 ? true : false;
    }
}