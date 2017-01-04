package com.example.ericdesedas.expohub.unit.data.network;

import com.example.ericdesedas.expohub.data.models.Category;
import com.example.ericdesedas.expohub.data.models.Comment;
import com.example.ericdesedas.expohub.data.models.EventType;
import com.example.ericdesedas.expohub.data.models.Fair;
import com.example.ericdesedas.expohub.data.models.FairEvent;
import com.example.ericdesedas.expohub.data.models.Map;
import com.example.ericdesedas.expohub.data.models.News;
import com.example.ericdesedas.expohub.data.models.Speaker;
import com.example.ericdesedas.expohub.data.models.Sponsor;
import com.example.ericdesedas.expohub.data.models.SponsorRank;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.helpers.FileReaderHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import moe.banana.jsonapi2.ResourceAdapterFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class JsonApiConverterTest {

    private Moshi moshi;
    private FileReaderHelper fileReader;

    @Before
    public void setUp() {

        JsonAdapter.Factory factory = ResourceAdapterFactory.builder()
                .add(Category.class)
                .add(Comment.class)
                .add(EventType.class)
                .add(Fair.class)
                .add(FairEvent.class)
                .add(Map.class)
                .add(News.class)
                .add(Speaker.class)
                .add(Sponsor.class)
                .add(SponsorRank.class)
                .add(Stand.class)
                .add(User.class)
                .strict()
                .build();

        moshi = new Moshi.Builder()
                .add(factory)
                .build();

        fileReader = new FileReaderHelper();
    }

    @Test
    public void it_parses_category_json_to_single_category() throws IOException {

        String jsonString = fileReader.readFile("json/category_single.json");

        Category category = moshi.adapter(Category.class).fromJson(jsonString);

        assertThat("Checking that category id is correct", category.getId(), is("1"));
        assertThat("Checking that category name is correct", category.name, is("foo"));
        assertThat("Checking that category's fair parsed correctly", category.fair.get(), is(not(nullValue())));
        assertThat("Checking that category contains correct amount of fairEvents", category.fairEvents.getAll().length, is(2));
    }

    @Test
    public void it_parses_categories_json_to_category_array() throws IOException {

        String jsonString = fileReader.readFile("json/category_list.json");

        Category[] categories = moshi.adapter(Category[].class).fromJson(jsonString);

        assertThat("Checking that category list has the correct size", categories.length, is(2));
    }

    @Test
    public void it_parses_comment_json_to_single_comment() throws IOException {

        String jsonString = fileReader.readFile("json/comment_single.json");

        Comment comment = moshi.adapter(Comment.class).fromJson(jsonString);

        assertThat("Checking that comment id is correct", comment.getId(), is("1"));
        assertThat("Checking that comment text is correct", comment.text, is("foo"));
        assertThat("Checking that comment's user parsed correctly", comment.user.get(), is(not(nullValue())));
        assertThat("Checking that comment's ownerNews parsed correctly", comment.ownerNews.get(), is(not(nullValue())));
    }

    @Test
    public void it_parses_comments_json_to_comment_array() throws IOException {

        String jsonString = fileReader.readFile("json/comment_list.json");

        Comment[] comments = moshi.adapter(Comment[].class).fromJson(jsonString);

        assertThat("Checking that comment list has the correct size", comments.length, is(3));
    }

    @Test
    public void it_parses_event_type_json_to_single_event_type() throws IOException {

        String jsonString = fileReader.readFile("json/event_type_single.json");

        EventType eventType = moshi.adapter(EventType.class).fromJson(jsonString);

        assertThat("Checking that event type id is correct", eventType.getId(), is("1"));
        assertThat("Checking that event type name is correct", eventType.name, is("aut"));
        assertThat("Checking that event type contains the correct amount of fairEvents", eventType.fairEvents.getAll().length, is(25));
    }

    @Test
    public void it_parses_event_types_json_to_event_type_array() throws IOException {

        String jsonString = fileReader.readFile("json/event_type_list.json");
        EventType[] eventTypes = moshi.adapter(EventType[].class).fromJson(jsonString);

        assertThat("Checking that event type array has the correct size", eventTypes.length, is(2));
    }

    @Test
    public void it_parses_fair_json_to_single_fair() throws IOException {

        String jsonString = fileReader.readFile("json/fair_single.json");
        Fair fair = moshi.adapter(Fair.class).fromJson(jsonString);

        assertThat("Checking that fair id is correct", fair.getId(), is("1"));
        assertThat("Checking that fair name is correct", fair.name, is("foo"));
        assertThat("Checking that fair image is correct", fair.image, is("bar"));
        assertThat("Checking that fair description is correct", fair.description, is("baz"));
        assertThat("Checking that fair website is correct", fair.website, is("qux"));
        assertThat("Checking that fair starting date is correct", fair.startingDate, is("fooBar"));
        assertThat("Checking that fair ending date is correct", fair.endingDate, is("barFoo"));
        assertThat("Checking that fair address is correct", fair.address, is("bazBaz"));
        assertThat("Checking that fair latitude is correct", fair.latitude, is(9.0));
        assertThat("Checking that fair longitude is correct", fair.longitude, is(10.0));
        assertThat("Checking that fair's user parsed correctly", fair.user.get(), is(not(nullValue())));
        assertThat("Checking that fair contains the correct amount of helperUsers", fair.helperUsers.getAll().length, is(1));
        assertThat("Checking that fair contains the correct amount of bannedUsers", fair.bannedUsers.getAll().length, is(1));
        assertThat("Checking that fair contains the correct amount of sponsors", fair.sponsors.getAll().length, is(2));
        assertThat("Checking that fair contains the correct amount of maps", fair.maps.getAll().length, is(3));
        assertThat("Checking that fair contains the correct amount of categories", fair.categories.getAll().length, is(1));
        assertThat("Checking that fair contains the correct amount of fairEvents", fair.fairEvents.getAll().length, is(2));
        assertThat("Checking that fair contains the correct amount of news", fair.news.getAll().length, is(2));
        assertThat("Checking that fair contains the correct amount of stands", fair.stands.getAll().length, is(2));
    }

    @Test
    public void it_parses_fairs_json_to_fair_array() throws IOException {

        String jsonString = fileReader.readFile("json/fair_list.json");
        Fair[] fairs = moshi.adapter(Fair[].class).fromJson(jsonString);

        assertThat("Checking that fair array has the correct size", fairs.length, is(4));
    }

    @Test
    public void it_parses_fair_event_json_to_single_fair_event() throws IOException {

        String jsonString = fileReader.readFile("json/fair_event_single.json");
        FairEvent fairEvent = moshi.adapter(FairEvent.class).fromJson(jsonString);

        assertThat("Checking that fairEvent id is correct", fairEvent.getId(), is("1"));
        assertThat("Checking that fairEvent title is correct", fairEvent.title, is("foo"));
        assertThat("Checking that fairEvent image is correct", fairEvent.image, is("bar"));
        assertThat("Checking that fairEvent description is correct", fairEvent.description, is("baz"));
        assertThat("Checking that fairEvent date is correct", fairEvent.date, is("qux"));
        assertThat("Checking that fairEvent location is correct", fairEvent.location, is("fooBar"));
        assertThat("Checking that fairEvent's fair parsed correctly", fairEvent.fair.get(), is(not(nullValue())));
        assertThat("Checking that fairEvent's eventType parsed correctly", fairEvent.eventType, is(not(nullValue())));
        assertThat("Checking that fairEvent contains correct amount of speakers", fairEvent.speakers.getAll().length, is(2));
        assertThat("Checking that fairEvent contains correct amount of attending users", fairEvent.attendingUsers.getAll().length, is(1));
        assertThat("Checking that fairEvent contains correct amount of categories", fairEvent.categories.getAll().length, is(1));
    }

    @Test
    public void it_parses_fair_events_json_to_fair_event_array() throws IOException {

        String jsonString = fileReader.readFile("json/fair_event_list.json");

        FairEvent[] fairEvents = moshi.adapter(FairEvent[].class).fromJson(jsonString);

        assertThat("Checking that fairEvent array has the correct size", fairEvents.length, is(2));
    }

    @Test
    public void it_parses_map_json_to_single_map() throws IOException {

        String jsonString = fileReader.readFile("json/map_single.json");

        Map map = moshi.adapter(Map.class).fromJson(jsonString);

        assertThat("Checking that map id is correct", map.getId(), is("1"));
        assertThat("Checking that map name is correct", map.name, is("foo"));
        assertThat("Checking that map image is correct", map.image, is("bar"));
        assertThat("Checking that map's fair parsed correctly", map.fair.get(), is(not(nullValue())));
    }

    @Test
    public void it_parses_maps_json_to_map_array() throws IOException {

        String jsonString = fileReader.readFile("json/map_list.json");

        Map[] maps = moshi.adapter(Map[].class).fromJson(jsonString);

        assertThat("Checking that map array has the correct size", maps.length, is(3));
    }

    @Test
    public void it_parses_news_json_to_single_news() throws IOException {

        String jsonString = fileReader.readFile("json/news_single.json");

        News news = moshi.adapter(News.class).fromJson(jsonString);

        assertThat("Checking that news id is correct", news.getId(), is("1"));
        assertThat("Checking that news title is correct", news.title, is("foo"));
        assertThat("Checking that news content is correct", news.content, is("bar"));
        assertThat("Checking that news image is correct", news.image, is("baz"));
        assertThat("Checking that news' fair parsed correctly", news.fair.get(), is(not(nullValue())));
        assertThat("Checking that news contains correct amount of comments", news.comments.getAll().length, is(2));
    }

    @Test
    public void it_parses_news_json_to_news_array() throws IOException {

        String jsonString = fileReader.readFile("json/news_list.json");

        News[] news = moshi.adapter(News[].class).fromJson(jsonString);

        assertThat("Checking that news array has the correct size", news.length, is(5));
    }

    @Test
    public void it_parses_speaker_json_to_single_speaker() throws IOException {

        String jsonString = fileReader.readFile("json/speaker_single.json");

        Speaker speaker = moshi.adapter(Speaker.class).fromJson(jsonString);

        assertThat("Checking that speaker has correct id", speaker.getId(), is("1"));
        assertThat("Checking that speaker has correct name", speaker.name, is("foo"));
        assertThat("Checking that speaker has correct picture", speaker.picture, is("bar"));
        assertThat("Checking that speaker has correct description", speaker.description, is("baz"));
        assertThat("Checking that speaker's fairEvent parsed correctly", speaker.fairEvent, is(not(nullValue())));
    }

    @Test
    public void it_parses_speakers_json_to_speaker_array() throws IOException {

        String jsonString = fileReader.readFile("json/speaker_list.json");

        Speaker[] speakers = moshi.adapter(Speaker[].class).fromJson(jsonString);

        assertThat("Checking that speakers array has the correct size", speakers.length, is(3));
    }

    @Test
    public void it_parses_sponsor_json_to_single_sponsor() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_single.json");

        Sponsor sponsor = moshi.adapter(Sponsor.class).fromJson(jsonString);

        assertThat("Checking that sponsor has correct id", sponsor.getId(), is("1"));
        assertThat("Checking that sponsor has correct name", sponsor.name, is("foo"));
        assertThat("Checking that sponsor has correct logo", sponsor.logo, is("bar"));
        assertThat("Checking that sponsor has correct slogan", sponsor.slogan, is("baz"));
        assertThat("Checking that sponsor has correct website", sponsor.website, is ("qux"));
        assertThat("Checking that sponsor's fair parsed correctly", sponsor.fair.get(), is(not(nullValue())));
        assertThat("Checking that sponsor's sponsorRank parsed correctly", sponsor.sponsorRank.get(), is(not(nullValue())));
    }

    @Test
    public void it_parses_sponsors_json_to_sponsor_array() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_list.json");

        Sponsor[] sponsors = moshi.adapter(Sponsor[].class).fromJson(jsonString);

        assertThat("Checking that sponsor array has the correct size", sponsors.length, is(3));
    }

    @Test
    public void it_parses_sponsor_rank_json_to_single_sponsor_rank() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_rank_single.json");

        SponsorRank sponsorRank = moshi.adapter(SponsorRank.class).fromJson(jsonString);

        assertThat("Checking that sponsorRank has correct id", sponsorRank.getId(), is("1"));
        assertThat("Checking that sponsorRank has correct name", sponsorRank.name, is("foo"));
        assertThat("Checking that sponsorRank contains correct amount of sponsors", sponsorRank.sponsors.getAll().length, is(3));
    }

    @Test
    public void it_parses_sponsor_ranks_json_to_sponsor_rank_array() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_rank_list.json");

        SponsorRank[] sponsorRanks = moshi.adapter(SponsorRank[].class).fromJson(jsonString);

        assertThat("Checking that sponsorRank array has the correct size", sponsorRanks.length, is(4));
    }

    @Test
    public void it_parses_stand_json_to_single_stand() throws IOException {

        String jsonString = fileReader.readFile("json/stand_single.json");

        Stand stand = moshi.adapter(Stand.class).fromJson(jsonString);

        assertThat("Checking that stand id is correct", stand.getId(), is("1"));
        assertThat("Checking that stand name is correct", stand.name, is("foo"));
        assertThat("Checking that stand description is correct", stand.description, is("bar"));
        assertThat("Checking that stand image is correct", stand.image, is("baz"));
        assertThat("Checking that stand's fair parsed correctly", stand.fair.get(), is(not(nullValue())));
    }

    @Test
    public void it_parses_stands_json_to_stand_array() throws IOException {

        String jsonString = fileReader.readFile("json/stand_list.json");

        Stand[] stands = moshi.adapter(Stand[].class).fromJson(jsonString);

        assertThat("Checking that stand array has the correct size", stands.length, is(2));
    }

    @Test
    public void it_parses_user_json_to_single_user() throws IOException {

        String jsonString = fileReader.readFile("json/user_single.json");

        User user = moshi.adapter(User.class).fromJson(jsonString);

        assertThat("Checking that user id is correct", user.getId(), is("1"));
        assertThat("Checking that user name is correct", user.name, is("foo"));
        assertThat("Checking that user username is correct", user.username, is("bar"));
        assertThat("Checking that user email is correct", user.email, is("baz"));
        assertThat("Checking that user contains correct amount of fairs", user.fairs.getAll().length, is(5));
        assertThat("Checking that user contains correct amount of helpingFairs", user.helpingFairs.getAll().length, is(3));
        assertThat("Checking that user contains correct amount of bannedFairs", user.bannedFairs.getAll().length, is(2));
        assertThat("Checking that user contains correct amount of attendingFairEvents", user.attendingFairEvents.getAll().length, is(3));
        assertThat("Checking that user contains correct amount of comments", user.comments.getAll().length, is(10));
    }

    @Test
    public void it_parses_users_json_to_user_array() throws IOException {

        String jsonString = fileReader.readFile("json/user_list.json");

        User[] users = moshi.adapter(User[].class).fromJson(jsonString);

        assertThat("Checking that user array has the correct size", users.length, is(2));
    }
}