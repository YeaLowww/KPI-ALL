# -*- coding: utf-8 -*-
"""text_lab8.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1SSB7HTHM_5Pxc0QeYI1yFe5dLwUI3klH
"""

import spacy
from spacy.matcher import Matcher
import json

# Завантаження файлу events.json
with open('events.json', 'r') as f:
    data = json.load(f)

# Завантаження моделі spaCy
nlp = spacy.load('en_core_web_sm')

# Ініціалізація класу Matcher
matcher = Matcher(nlp.vocab)

team_patterns = [
    [{"LOWER": {"IN": ["fc", "team", "club"]}}, {"POS": "PROPN"}],
    [{"POS": "PROPN"}, {"LOWER": "vs"}, {"POS": "PROPN"}],
]

# Додавання шаблонів для різних варіантів підтверджень
confirm_patterns = [
    [{"LOWER": {"IN": ["awesome", "great", "fantastic", "amazing"]}}],
    [{"LOWER": {"IN": ["sounds", "looks", "seems", "feels"]}}, {"LOWER": "good"}],
    [{"LOWER": {"IN": ["sounds", "looks", "seems", "feels"]}}, {"LOWER": "great"}],
]

# Додавання шаблонів до Matcher
for pattern in team_patterns:
    matcher.add("TEAM", [pattern])

for pattern in confirm_patterns:
    matcher.add("CONFIRM", [pattern])

# Функція для виділення сутностей та підтверджень
def extract_entities(text):
    doc = nlp(text)
    matches = matcher(doc)
    entities = []
    confirms = []
    for match_id, start, end in matches:
        if nlp.vocab.strings[match_id] == "TEAM":
            entities.append(doc[start:end].text)
        elif nlp.vocab.strings[match_id] == "CONFIRM":
            confirms.append(doc[start:end].text)
    return entities, confirms

def extract_intents(text):
    doc = nlp(text)
    intents = []
    for token in doc:
        if token.dep_ == "dobj":
            dobj = token.text
            verb = token.head.text
            conjuncts = [t.text for t in token.head.children if t.dep_ == "conj"]
            intents.append((verb, dobj, conjuncts))
    return intents

# Приклад використання
for event in data:
    for turn in event["turns"]:
        entities, confirms = extract_entities(turn["utterance"])
        intents = extract_intents(turn["utterance"])
        print(f"Turn: {turn['utterance']}")
        print("Entities:", entities)
        print("Confirms:", confirms)
        for intent in intents:
          print("Verb:", intent[0])
          print("Direct Object:", intent[1])
          print("Conjuncts:", intent[2])
        print("-" * 30)

"""Завдання до лабораторної роботи
1. Використати клас Matcher для виділення сутностей відповідно до
варіанту. Використати файл з 7-ї лабораторної роботи (не обов’язково усі
висловлювання). Продемонструвати роботу.
2. Застосувати синтаксичні залежності для визначення намірів.
Використати файл з 7-ї лабораторної роботи (не обов’язково усі
висловлювання).
"""