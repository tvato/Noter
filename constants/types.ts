import Setting from "@/db_tools/Setting";

export interface Rows{
    id: number;
    content: string;
    note: number;
    checked: boolean;
}

export interface Notes {
    id: number;
    title: string;
}

export interface Settings{
    id: number;
    backgroundColor: string;
    textColor: string;
    checkedTextColor: string;
    borderColorNote: string;
    borderColorNewNote: string;
    backgroundColorNewNote: string;
}

export type NoteViewParams = {
    noteID: number;
    noteTitle: string;
    rows: Rows[];
}

export type RootStackParamList = {
    Home: {settings: Setting};
    NoteView: {
        noteID: number,
        noteTitle: string,
        rows: Rows[],
        settings: Setting,
    };
    Settings: {settings: Setting};
}

export type HeaderOptions = {
    headerStyle: {
        backgroundColor: string
    },
    headerTintColor: string,
    headerTitle: string
}