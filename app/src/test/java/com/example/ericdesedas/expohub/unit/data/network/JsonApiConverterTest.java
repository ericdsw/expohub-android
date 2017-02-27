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
import com.example.ericdesedas.expohub.data.models.Unknown;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.helpers.FileReaderHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import moe.banana.jsonapi2.Document;
import moe.banana.jsonapi2.ResourceAdapterFactory;
import moe.banana.jsonapi2.ResourceIdentifier;

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
                .add(Unknown.class)
                .build();

        moshi = new Moshi.Builder()
                .add(factory)
                .build();

        fileReader = new FileReaderHelper();
    }

    @Test
    public void it_parses_category_json_to_single_category() throws IOException {

        String jsonString = fileReader.readFile("json/category_single.json");

        Category category = parseToDocument(jsonString, Category.class).get();

        assertThat("Checking that category id is correct", category.getId(), is("1"));
        assertThat("Checking that category name is correct", category.name, is("foo"));
        assertThat("Checking that category's fair parsed correctly", category.getFair(), is(not(nullValue())));
        assertThat("Checking that category contains correct amount of fairEvents", category.getFairEvents().size(), is(2));
    }

    @Test
    public void it_parses_categories_json_to_category_array() throws IOException {

        String jsonString = fileReader.readFile("json/category_list.json");

        Document<Category> categoryDocument = parseToDocument(jsonString, Category.class);

        assertThat("Checking that category list has the correct size", categoryDocument.size(), is(2));
    }

    @Test
    public void it_parses_comment_json_to_single_comment() throws IOException {

        String jsonString = fileReader.readFile("json/comment_single.json");

        Comment comment = parseToDocument(jsonString, Comment.class).get();

        assertThat("Checking that comment id is correct", comment.getId(), is("1"));
        assertThat("Checking that comment text is correct", comment.text, is("foo"));
        assertThat("Checking that comment's user parsed correctly", comment.getUser(), is(not(nullValue())));
        assertThat("Checking that comment's ownerNews parsed correctly", comment.getOwnerNews(), is(not(nullValue())));
    }

    @Test
    public void it_parses_comments_json_to_comment_array() throws IOException {

        String jsonString = fileReader.readFile("json/comment_list.json");

        Document<Comment> commentsDocument = parseToDocument(jsonString, Comment.class);

        assertThat("Checking that comment list has the correct size", commentsDocument.size(), is(3));
    }

    @Test
    public void it_parses_event_type_json_to_single_event_type() throws IOException {

        String jsonString = fileReader.readFile("json/event_type_single.json");

        EventType eventType = parseToDocument(jsonString, EventType.class).get();

        assertThat("Checking that event type id is correct", eventType.getId(), is("1"));
        assertThat("Checking that event type name is correct", eventType.name, is("aut"));
        assertThat("Checking that event type contains the correct amount of fairEvents", eventType.getFairEvent().size(), is(25));
    }

    @Test
    public void it_parses_event_types_json_to_event_type_array() throws IOException {

        String jsonString = fileReader.readFile("json/event_type_list.json");

        Document<EventType> eventTypesDocument = parseToDocument(jsonString, EventType.class);

        assertThat("Checking that event type array has the correct size", eventTypesDocument.size(), is(2));
    }

    @Test
    public void it_parses_fair_json_to_single_fair() throws IOException {

        String jsonString = fileReader.readFile("json/fair_single.json");

        Fair fair = parseToDocument(jsonString, Fair.class).get();

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
        assertThat("Checking that fair's user parsed correctly", fair.getUser(), is(not(nullValue())));
        assertThat("Checking that fair contains the correct amount of helperUsers", fair.getHelperUsers().size(), is(1));
        assertThat("Checking that fair contains the correct amount of bannedUsers", fair.getBannedUsers().size(), is(1));
        assertThat("Checking that fair contains the correct amount of sponsors", fair.getSponsors().size(), is(2));
        assertThat("Checking that fair contains the correct amount of maps", fair.getMaps().size(), is(3));
        assertThat("Checking that fair contains the correct amount of categories", fair.getCategories().size(), is(1));
        assertThat("Checking that fair contains the correct amount of fairEvents", fair.getFairEvents().size(), is(2));
        assertThat("Checking that fair contains the correct amount of news", fair.getNewses().size(), is(2));
        assertThat("Checking that fair contains the correct amount of stands", fair.getStands().size(), is(2));
    }

    @Test
    public void it_parses_fairs_json_to_fair_array() throws IOException {

        String jsonString = fileReader.readFile("json/fair_list.json");

        Document<Fair> fairsDocument = parseToDocument(jsonString, Fair.class);

        assertThat("Checking that fair array has the correct size", fairsDocument.size(), is(4));
    }

    @Test
    public void it_parses_fair_event_json_to_single_fair_event() throws IOException {

        String jsonString = fileReader.readFile("json/fair_event_single.json");

        FairEvent fairEvent = parseToDocument(jsonString, FairEvent.class).get();

        assertThat("Checking that fairEvent id is correct", fairEvent.getId(), is("1"));
        assertThat("Checking that fairEvent title is correct", fairEvent.title, is("foo"));
        assertThat("Checking that fairEvent image is correct", fairEvent.image, is("bar"));
        assertThat("Checking that fairEvent description is correct", fairEvent.description, is("baz"));
        assertThat("Checking that fairEvent date is correct", fairEvent.date, is("qux"));
        assertThat("Checking that fairEvent location is correct", fairEvent.location, is("fooBar"));
        assertThat("Checking that fairEvent's fair parsed correctly", fairEvent.getFair(), is(not(nullValue())));
        assertThat("Checking that fairEvent's eventType parsed correctly", fairEvent.getEventType(), is(not(nullValue())));
        assertThat("Checking that fairEvent contains correct amount of speakers", fairEvent.getSpeakers().size(), is(2));
        assertThat("Checking that fairEvent contains correct amount of attending users", fairEvent.getAttendingUsers().size(), is(1));
        assertThat("Checking that fairEvent contains correct amount of categories", fairEvent.getCategories().size(), is(1));
    }

    @Test
    public void it_parses_fair_events_json_to_fair_event_array() throws IOException {

        String jsonString = fileReader.readFile("json/fair_event_list.json");

        Document<FairEvent> fairEventsDocument = parseToDocument(jsonString, FairEvent.class);

        assertThat("Checking that fairEvent array has the correct size", fairEventsDocument.size(), is(2));
    }

    @Test
    public void it_parses_map_json_to_single_map() throws IOException {

        String jsonString = fileReader.readFile("json/map_single.json");

        Map map = parseToDocument(jsonString, Map.class).get();

        assertThat("Checking that map id is correct", map.getId(), is("1"));
        assertThat("Checking that map name is correct", map.name, is("foo"));
        assertThat("Checking that map image is correct", map.image, is("bar"));
        assertThat("Checking that map's fair parsed correctly", map.fair.get(), is(not(nullValue())));
    }

    @Test
    public void it_parses_maps_json_to_map_array() throws IOException {

        String jsonString = fileReader.readFile("json/map_list.json");

        Document<Map> mapsDocument = parseToDocument(jsonString, Map.class);

        assertThat("Checking that map array has the correct size", mapsDocument.size(), is(3));
    }

    @Test
    public void it_parses_news_json_to_single_news() throws IOException {

        String jsonString = fileReader.readFile("json/news_single.json");

        News news = parseToDocument(jsonString, News.class).get();

        assertThat("Checking that news id is correct", news.getId(), is("1"));
        assertThat("Checking that news title is correct", news.title, is("foo"));
        assertThat("Checking that news content is correct", news.content, is("bar"));
        assertThat("Checking that news image is correct", news.image, is("baz"));
        assertThat("Checking that news' fair parsed correctly", news.fair.get(), is(not(nullValue())));
        assertThat("Checking that news contains correct amount of comments", news.getComments().size(), is(2));
    }

    @Test
    public void it_parses_news_json_to_news_array() throws IOException {

        String jsonString = fileReader.readFile("json/news_list.json");

        Document<News> newsDocument = parseToDocument(jsonString, News.class);

        assertThat("Checking that news array has the correct size", newsDocument.size(), is(5));
    }

    @Test
    public void it_parses_speaker_json_to_single_speaker() throws IOException {

        String jsonString = fileReader.readFile("json/speaker_single.json");

        Speaker speaker = parseToDocument(jsonString, Speaker.class).get();

        assertThat("Checking that speaker has correct id", speaker.getId(), is("1"));
        assertThat("Checking that speaker has correct name", speaker.name, is("foo"));
        assertThat("Checking that speaker has correct picture", speaker.picture, is("bar"));
        assertThat("Checking that speaker has correct description", speaker.description, is("baz"));
        assertThat("Checking that speaker's fairEvent parsed correctly", speaker.getFairEvent(), is(not(nullValue())));
    }

    @Test
    public void it_parses_speakers_json_to_speaker_array() throws IOException {

        String jsonString = fileReader.readFile("json/speaker_list.json");

        Document<Speaker> speakersDocument = parseToDocument(jsonString, Speaker.class);

        assertThat("Checking that speakers array has the correct size", speakersDocument.size(), is(3));
    }

    @Test
    public void it_parses_sponsor_json_to_single_sponsor() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_single.json");

        Sponsor sponsor = parseToDocument(jsonString, Sponsor.class).get();

        assertThat("Checking that sponsor has correct id", sponsor.getId(), is("1"));
        assertThat("Checking that sponsor has correct name", sponsor.name, is("foo"));
        assertThat("Checking that sponsor has correct logo", sponsor.logo, is("bar"));
        assertThat("Checking that sponsor has correct slogan", sponsor.slogan, is("baz"));
        assertThat("Checking that sponsor has correct website", sponsor.website, is ("qux"));
        assertThat("Checking that sponsor's fair parsed correctly", sponsor.getFair(), is(not(nullValue())));
        assertThat("Checking that sponsor's sponsorRank parsed correctly", sponsor.getSponsorRank(), is(not(nullValue())));
    }

    @Test
    public void it_parses_sponsors_json_to_sponsor_array() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_list.json");

        Document<Sponsor> sponsorsDocument = parseToDocument(jsonString, Sponsor.class);

        assertThat("Checking that sponsor array has the correct size", sponsorsDocument.size(), is(3));
    }

    @Test
    public void it_parses_sponsor_rank_json_to_single_sponsor_rank() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_rank_single.json");

        SponsorRank sponsorRank = parseToDocument(jsonString, SponsorRank.class).get();

        assertThat("Checking that sponsorRank has correct id", sponsorRank.getId(), is("1"));
        assertThat("Checking that sponsorRank has correct name", sponsorRank.name, is("foo"));
        assertThat("Checking that sponsorRank contains correct amount of sponsors", sponsorRank.getSponsors().size(), is(3));
    }

    @Test
    public void it_parses_sponsor_ranks_json_to_sponsor_rank_array() throws IOException {

        String jsonString = fileReader.readFile("json/sponsor_rank_list.json");

        Document<SponsorRank> sponsorRanksDocument = parseToDocument(jsonString, SponsorRank.class);

        assertThat("Checking that sponsorRank array has the correct size", sponsorRanksDocument.size(), is(4));
    }

    @Test
    public void it_parses_stand_json_to_single_stand() throws IOException {

        String jsonString = fileReader.readFile("json/stand_single.json");

        Stand stand = parseToDocument(jsonString, Stand.class).get();

        assertThat("Checking that stand id is correct", stand.getId(), is("1"));
        assertThat("Checking that stand name is correct", stand.name, is("foo"));
        assertThat("Checking that stand description is correct", stand.description, is("bar"));
        assertThat("Checking that stand image is correct", stand.image, is("baz"));
        assertThat("Checking that stand's fair parsed correctly", stand.getFair(), is(not(nullValue())));
    }

    @Test
    public void it_parses_stands_json_to_stand_array() throws IOException {

        String jsonString = fileReader.readFile("json/stand_list.json");

        Document<Stand> standsDocument = parseToDocument(jsonString, Stand.class);

        assertThat("Checking that stand array has the correct size", standsDocument.size(), is(2));
    }

    @Test
    public void it_parses_user_json_to_single_user() throws IOException {

        String jsonString = fileReader.readFile("json/user_single.json");

        User user = parseToDocument(jsonString, User.class).get();

        assertThat("Checking that user id is correct", user.getId(), is("1"));
        assertThat("Checking that user name is correct", user.name, is("foo"));
        assertThat("Checking that user username is correct", user.username, is("bar"));
        assertThat("Checking that user email is correct", user.email, is("baz"));
        assertThat("Checking that user contains correct amount of fairs", user.getFairs().size(), is(5));
        assertThat("Checking that user contains correct amount of helpingFairs", user.getHelpingFairs().size(), is(3));
        assertThat("Checking that user contains correct amount of bannedFairs", user.getBannedFairs().size(), is(2));
        assertThat("Checking that user contains correct amount of attendingFairEvents", user.getAttendingFairEvents().size(), is(3));
        assertThat("Checking that user contains correct amount of comments", user.getComments().size(), is(10));
    }

    @Test
    public void it_parses_users_json_to_user_array() throws IOException {

        String jsonString = fileReader.readFile("json/user_list.json");

        Document<User> usersDocument = parseToDocument(jsonString, User.class);

        assertThat("Checking that user array has the correct size", usersDocument.size(), is(2));
    }

    @Test
    public void it_parses_null_to_unknown() throws IOException {

        String jsonString = fileReader.readFile("json/empty_response.json");

        Document<Unknown> randomDocument = parseToDocument(jsonString, Unknown.class);

        assertThat("Checking that conversion returned null", randomDocument.get(), is(nullValue()));
    }

    /**
     * Returns a document representation from the provided string
     *
     * @param json          a {@link String} reference with the string to transform
     * @param tClass        the {@link Class<T>} related to the return parameter
     * @param <T>           the class reference that extends
     * @return              a {@link Document<T>} instance containing transformed data
     * @throws IOException  when the json is invalid
     */
    private <T extends ResourceIdentifier> Document<T> parseToDocument(String json, Class<T> tClass) throws IOException {

        Type type                           = Types.newParameterizedType(Document.class, tClass);
        JsonAdapter<Document<T>> adapter    = moshi.adapter(type);

        return adapter.fromJson(json);
    }
}